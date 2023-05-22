package ca.mcgillcssa.cssabackend.controller;

import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.mcgillcssa.cssabackend.dto.SponsorDTO;
import ca.mcgillcssa.cssabackend.model.Sponsor;
import ca.mcgillcssa.cssabackend.service.SponsorService;
import lombok.Data;
import lombok.ToString;

@RestController
@RequestMapping("/sponsors")
public class SponsorController {

    @Data
    @ToString
    public static class SponsorRequestBody {
        private String sponsorName;
        private String coopDuration;
        private String sponsorImageUrl;
        private String sponsorWebsiteUrl;
        private String sponsorClass;
    }

    private final SponsorService sponsorService;
    private static final String msgStr = "message";
    private static final String sponsorStr = "sponsor";

    public SponsorController(SponsorService memberService) {
        this.sponsorService = memberService;
    }

    /**
     * Creates a new sponsor in the database.
     * 
     * @Author Zihan Zhang
     * @param requestBody contains sponsor name, coop duration, sponsor image url,
     *                    sponsor website url, and sponsor class
     * @return a response containing the saved sponsor object
     */
    @PostMapping("/")
    public ResponseEntity<?> createSponsor(@RequestBody SponsorRequestBody requestBody) {
        Map<String, Object> response = new HashMap<>();
        try {
            Sponsor newSponsor = sponsorService.createSponsor(requestBody.getSponsorName(),
                    requestBody.getCoopDuration(),
                    requestBody.getSponsorImageUrl(), requestBody.getSponsorWebsiteUrl(),
                    requestBody.getSponsorClass());
            response.put(msgStr, "Sponsor created");
            response.put(sponsorStr, new SponsorDTO(newSponsor));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            response.put(msgStr, "Failed to create sponsor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (DataAccessException | IOException e) {
            // TODO: remove exception message from response after debugging
            response.put(msgStr, "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves all sponsors in the database.
     * 
     * @Author Zihan Zhang
     * @return a response containing a list of all sponsors
     */
    @GetMapping("/")
    public ResponseEntity<?> getAllSponsors() {
        List<Sponsor> sponsors = new ArrayList<>();
        try {
            sponsors = sponsorService.findAllSponsors();
            return ResponseEntity.status(HttpStatus.OK).body(sponsors);
        } catch (Exception e) {
            // TODO: remove exception message from response after debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Retrieves a sponsor by name.
     * 
     * @Author Zihan Zhang
     * @param sponsorName the name of the sponsor
     * @return a response containing a sponsor object
     */
    @GetMapping("/name/{sponsorName}")
    public ResponseEntity<?> getSponsorByName(@PathVariable String sponsorName) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Sponsor> sponsor = sponsorService.findSponsorByName(sponsorName);
            if (sponsor.isPresent()) {
                response.put(msgStr, "Sponsor found with name: " + sponsorName);
                response.put(sponsorStr, new SponsorDTO(sponsor.get()));
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.put(msgStr, "Sponsor not found with name: " + sponsorName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put(msgStr, "Failed to find sponsor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    /**
     * Retrieves a sponsor by coop duration.
     * 
     * @Author Zihan Zhang
     * @param coopDuration the coop duration of the sponsor
     * @return a response containing a list of sponsor object
     */
    @GetMapping("/duration/{coopDuration}")
    public ResponseEntity<?> getSponsorByCoopDuration(@PathVariable String coopDuration) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Sponsor> sponsor = sponsorService.findSponsorsByCoopDuration(coopDuration);
            if (!sponsor.isEmpty()) {
                List<SponsorDTO> sponsorDTO = new ArrayList<>();
                for (Sponsor s : sponsor) {
                    sponsorDTO.add(new SponsorDTO(s));
                }
                response.put(msgStr, "Sponsors found with coop duration: " + coopDuration);
                response.put("sponsors", sponsorDTO);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.put(msgStr, "Sponsors not found with coop duration: " + coopDuration);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put(msgStr, "Failed to find sponsor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Retrieves a sponsor by sponsor class.
     * 
     * @Author Zihan Zhang
     * @param sponsorClass the sponsor class of the sponsor
     * @return a response containing a list of sponsor object
     */
    @GetMapping("/class/{sponsorClass}")
    public ResponseEntity<?> getSponsorByClass(@PathVariable String sponsorClass) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Sponsor> sponsor = sponsorService.findSponsorsByClass(sponsorClass);
            if (!sponsor.isEmpty()) {
                List<SponsorDTO> sponsorDTO = new ArrayList<>();
                for (Sponsor s : sponsor) {
                    sponsorDTO.add(new SponsorDTO(s));
                }
                response.put(msgStr, "Sponsors found with sponsor class: " + sponsorClass);
                response.put("sponsors", sponsorDTO);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.put(msgStr, "Sponsors not found with sponsor class: " + sponsorClass);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put(msgStr, "Failed to find sponsor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Deletes a sponsor by name.
     * 
     * @Author Zihan Zhang
     * @param sponsorName the name of the sponsor
     * @return a response containing a sponsor object if deleted successfully
     */
    @DeleteMapping("/name/{sponsorName}")
    public ResponseEntity<?> deleteSponsorByName(@PathVariable String sponsorName) {
        Map<String, Object> response = new HashMap<>();
        boolean deleted = sponsorService.deleteSponsorByName(sponsorName);
        if (deleted) {
            response.put(msgStr, "Sponsor deleted with name: " + sponsorName);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.put(msgStr, "Sponsor not found with name: " + sponsorName);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Updates a sponsor by name.
     * 
     * @Author Zihan Zhang
     * @param sponsorName the name of the sponsor
     * @param requestBody the request body, containing the corp duration, sponsor image url, sponsor website url, sponsor class
     * @return a response containing a sponsor object if updated successfully
     **/

    @PutMapping("/name/{sponsorName}")
    public ResponseEntity<?> updateSponsorByName(@PathVariable String sponsorName,
            @RequestBody SponsorRequestBody requestBody) throws IOException {
        Map<String, Object> response = new HashMap<>();
        try {
            sponsorService.updateSponsor(sponsorName,
                    requestBody.getCoopDuration(),
                    requestBody.getSponsorImageUrl(), requestBody.getSponsorWebsiteUrl(),
                    requestBody.getSponsorClass());
            response.put(msgStr, "Sponsor updated with name: " + sponsorName);
            response.put(sponsorStr, new SponsorDTO(sponsorService.findSponsorByName(sponsorName).get()));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            response.put(msgStr, "Failed to update sponsor with name: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
