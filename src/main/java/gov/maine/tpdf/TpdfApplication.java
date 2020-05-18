package gov.maine.tpdf;

import gov.maine.tpdf.config.Constants;
import gov.maine.tpdf.service.ProcessDebtFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class TpdfApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(TpdfApplication.class, args);
	}

	private static final Logger logger = LoggerFactory.getLogger(TpdfApplication.class);

	@Autowired
	private ProcessDebtFile processDebtFile;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		String fileName = args.getOptionValues("fileName").get(0);

		processDebtFile(fileName);

//		if(args.getOptionNames() != null && args.getOptionNames().size() > 0) {
//
//			logger.info("Application started with command-line arguments: {}", Arrays.toString(args.getSourceArgs()));
//			logger.info("NonOptionArgs: {}", args.getNonOptionArgs());
//			logger.info("OptionNames: {}", args.getOptionNames());
//
//			for (String name : args.getOptionNames()){
//				logger.info("arg-" + name + "=" + args.getOptionValues(name));
//			}
//
//			String job = args.getOptionValues("job").get(0).toLowerCase();
//
//			switch (job) {
//				case Constants.JobConstants.PROCESS_DEBT_FILE:
//					processDebtFile();
//				default:
//					defaultJob();
//					break;
//			}
//		}else{
//			System.out.println("Executed without Args");
//		}
	}


	private void defaultJob(){
		System.out.println("No jobs to process");
	}

	private void processDebtFile(String fileName){
		processDebtFile.start(fileName);
	}
}
