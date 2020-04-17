package com.corona.coronavirustracker.services;

import com.corona.coronavirustracker.models.DeathDataLocationStats;
import com.corona.coronavirustracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CoronaVirusDataService {

    private static final Logger logger = LoggerFactory.getLogger(CoronaVirusDataService.class.getName());
    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private static String VIRUS_DATA_URL_DEATH = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";

    private List<LocationStats> allStats = new ArrayList<>();

    private List<DeathDataLocationStats> deathStats = new ArrayList<>();

    public List<LocationStats> getAllStats() {
        return allStats;
    }

    public List<DeathDataLocationStats> getDeathStats() {
        return deathStats;
    }

    @PostConstruct
   @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
      logger.error("fetchVirusData is called");
        List<LocationStats> newStats = new ArrayList<>();
        List<DeathDataLocationStats> newDeathStats = new ArrayList<>();
        RestTemplateBuilder restTemplate = new RestTemplateBuilder();
        ResponseEntity<String> totalVirusDataresponse = restTemplate.build().getForEntity(VIRUS_DATA_URL, String.class);
        ResponseEntity<String> deathVirusDataResponse = restTemplate.build().getForEntity(VIRUS_DATA_URL_DEATH, String.class);

        System.out.println(totalVirusDataresponse.getBody());
        logger.error("fetchVirusData data is " + totalVirusDataresponse.getBody());
        StringReader csvBodyReader = new StringReader(totalVirusDataresponse.getBody());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        StringReader deathCSVBodyReader = new StringReader(deathVirusDataResponse.getBody());
        Iterable<CSVRecord>  deathRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(deathCSVBodyReader);

        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
            locationStat.setLatestTotalCases(latestCases);
            locationStat.setDiffFromPrevDay(latestCases - prevDayCases);
            newStats.add(locationStat);
        }
        this.allStats = newStats;
        for (CSVRecord record : deathRecords) {
            DeathDataLocationStats deathDataLocationStats = new DeathDataLocationStats();
            deathDataLocationStats.setState(record.get("Province/State"));
            deathDataLocationStats.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
            deathDataLocationStats.setLatestTotalCases(latestCases);
            deathDataLocationStats.setDiffFromPrevDay(latestCases - prevDayCases);
            Optional<LocationStats> stats =  newStats.stream().filter(a->a.getCountry().equals(deathDataLocationStats.getCountry())
                             && a.getState().equals(deathDataLocationStats.getState())).findFirst();
            if(stats.isPresent()) {
                stats.get().setLatestDeathCases(deathDataLocationStats.getLatestTotalCases());
                stats.get().setDeathDiffFromPrevDay(deathDataLocationStats.getDiffFromPrevDay());
            }
            newDeathStats.add(deathDataLocationStats);
        }
        this.deathStats = newDeathStats;
    }

}
