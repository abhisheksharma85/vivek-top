package gov.maine.tpdf.repository;


import gov.maine.tpdf.TpdfApplication;
import gov.maine.tpdf.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomJDBCRepository {


    private static final Logger logger = LoggerFactory.getLogger(TpdfApplication.class);

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public boolean checkState(String stateCode){

        logger.info("Check for stateCode {}",stateCode);

        StringBuilder sql = new StringBuilder(" Select id from  ").append(Constants.TableConstants.TAX_1_REF)
                .append(" where state_code = :stateCode ");

        List<Long> stateIds = namedParameterJdbcTemplate.queryForList(sql.toString(),
                new MapSqlParameterSource("stateCode", stateCode),Long.class);

        return stateIds.size() == 0 ? false : true;

    }



}
