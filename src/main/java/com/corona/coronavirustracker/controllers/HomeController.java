package com.corona.coronavirustracker.controllers;

import com.corona.coronavirustracker.models.DeathDataLocationStats;
import com.corona.coronavirustracker.services.CoronaVirusDataService;
import com.corona.coronavirustracker.models.LocationStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/")
    public String home(Model model) {
        List<LocationStats> allStats = coronaVirusDataService.getAllStats();
        List<DeathDataLocationStats> deathStats = coronaVirusDataService.getDeathStats();
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
        int totalDeathCases = deathStats.stream().mapToInt(stat->stat.getLatestTotalCases()).sum();
        int totalNewDeathCases = deathStats.stream().mapToInt(stat->stat.getDiffFromPrevDay()).sum();
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalDeathCases", totalDeathCases);
        model.addAttribute("totalNewDeathCases", totalNewDeathCases);
        model.addAttribute("totalNewCases", totalNewCases);

        return "home";
    }
}
