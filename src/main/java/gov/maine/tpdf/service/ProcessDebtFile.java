package gov.maine.tpdf.service;

import gov.maine.tpdf.TpdfApplication;
import gov.maine.tpdf.config.Constants;
import gov.maine.tpdf.repository.CustomJDBCRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ProcessDebtFile {

    private static final Logger logger = LoggerFactory.getLogger(TpdfApplication.class);


    @Autowired
    private CustomJDBCRepository customJDBCRepository;

    public void start(String fileName){
        logger.info("Request to Process Debt File Started");
        logger.info("Processing File {} ",fileName);

        StringBuilder srcFilePath = new StringBuilder(Constants.SOURCE_FILE_DIR).append(fileName);

        File file = new File(srcFilePath.toString());

        if(file.exists()) {
            StringBuilder destFilePath = new StringBuilder(Constants.DESTINATION_FILE_DIR).append(fileName);

            StringBuilder processedData = new StringBuilder();
            File newFile = new File(destFilePath.toString());

            BufferedReader reader;
            BufferedWriter output = null;

            StringBuilder agencyId = new StringBuilder();

            StringBuilder agencySiteId = new StringBuilder();
            StringBuilder debtNumber = new StringBuilder();
            StringBuilder action = new StringBuilder();
            StringBuilder recordType = new StringBuilder();
            StringBuilder paymentBypassIndicators = new StringBuilder();

            int counter = 0;
            try {
                reader = new BufferedReader(new FileReader(srcFilePath.toString()));
                String line = reader.readLine();
                while (line != null) {
                    //Header
                    if(line.startsWith(Constants.HEADER_CHECK)){
                        System.out.println("Header found");
                    }
                    //Body || Detail
                    if(line.startsWith(Constants.ME)){

                        agencyId.append(line.substring(0,7).replaceAll("\\u00A0",""));
                        agencySiteId.append(line.substring(8,15).replaceAll("\\u00A0",""));
                        debtNumber.append(line.substring(16,33).replaceAll("\\u00A0",""));
                        //Check if Address belongs to State of Maine
                        boolean existInMaine = customJDBCRepository.checkState(agencyId.toString().trim());
                        System.out.println("existInMaine "+existInMaine);

                        //Add it to new File
                        if(existInMaine){
                            processedData.append(line).append("\n");
                        }

                        //Clear all StringBuilder to reuse
                        agencyId.setLength(0);
                        agencySiteId.setLength(0);
                        debtNumber.setLength(0);
                        action.setLength(0);
                        recordType.setLength(0);
                        paymentBypassIndicators.setLength(0);

                    }
                    //Footer
                    if(line.startsWith(Constants.FOOTER_CHECK)){
                        System.out.println("footer found");
                    }
                    line = reader.readLine();
                }
            }catch (FileNotFoundException fileNotFoundException){
                fileNotFoundException.printStackTrace();
            }catch (IOException ioException){
                ioException.printStackTrace();
            }

            try {
                output = new BufferedWriter(new FileWriter(newFile));
                output.write(processedData.toString());

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (output != null) {
                    try {
                        output.close();
                        processedData.delete(0, processedData.length());
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }else {
            logger.debug("No file exists with name {} ",fileName);
            System.out.println("No file exists");
        }
        logger.info("Request Completed");
    }
}
