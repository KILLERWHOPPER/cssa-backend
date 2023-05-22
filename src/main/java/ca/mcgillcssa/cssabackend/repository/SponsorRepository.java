package ca.mcgillcssa.cssabackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import ca.mcgillcssa.cssabackend.model.Sponsor;

@Repository
public class SponsorRepository {

    private final MongoTemplate mongoTemplate;

    public SponsorRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Saves a new sponsor in the database.
     * @Author Zihan Zhang
     * @param sponsor the sponsor object to be saved
     * @return the saved sponsor object
     */
    public Sponsor createSponsor(Sponsor sponsor) {
        return mongoTemplate.save(sponsor);
    }

    /**
     * Finds a Sponsor in the database by its name.
     * @Author Zihan Zhang
     * @param sponsorName the name of the Sponsor to search for
     * @return Optional containing the Sponsor if found, otherwise empty
     */
    public Optional<Sponsor> findSponsorByName(String sponsorName) {
        Query query = new Query(Criteria.where("sponsorName").is(sponsorName));
        return Optional.ofNullable(mongoTemplate.findOne(query, Sponsor.class));
    }

    /**
     * Retrieves all sponsors using the mongoTemplate.
     * @Author Zihan Zhang
     * @return a list of Sponsor objects
     */
    public List<Sponsor> findAllSponsors() {
        return mongoTemplate.findAll(Sponsor.class);
    }

    /**
     * Finds sponsors by sponsor class using the mongoTemplate.
     * @Author Zihan Zhang
     * @param sponsorClass the class of the sponsor to search for
     * @return a list of Sponsor objects matching the sponsor class
     */
    public List<Sponsor> findSponsorsByClass(String sponsorClass) {
        return mongoTemplate.find(new Query(Criteria.where("sponsorClass").is(sponsorClass)), Sponsor.class);
    }

    /**
     * Finds sponsors by coop duration using the mongoTemplate.'
     * @Author Zihan Zhang
     * @param coopDuration the coop duration of the sponsor to search for
     * @return a list of Sponsor objects matching the coop duration
     * */
    public List<Sponsor> findSponsorsByCoopDuration(String coopDuration) {
        return mongoTemplate.find(new Query(Criteria.where("coopDuration").is(coopDuration)), Sponsor.class);
    }

    /**
     * Deletes a sponsor from the database.
     * @Author Zihan Zhang
     * @param sponsorName the name of the sponsor to delete
     * @return true if the sponsor was deleted, false otherwise
     * */
    public boolean deleteSponsorByName(String sponsorName) {
        Query query = new Query(Criteria.where("sponsorName").is(sponsorName));
        DeleteResult deleteResult = mongoTemplate.remove(query, Sponsor.class);
        return deleteResult.wasAcknowledged() && deleteResult.getDeletedCount() > 0;
    }

    /**
     * Updates a sponsor in the database.
     * @Author Zihan Zhang
     * @param name the name of the sponsor to update
     * @param coopDuration the coop duration of the sponsor to update
     * @param sponsorImageUrl the image url of the sponsor to update
     * @param sponsorWebsiteUrl the website url of the sponsor to update
     * @param sponsorClass the class of the sponsor to update
     * @return true if the sponsor was updated, false otherwise
     * */
    public boolean updateSponsor(String name, String coopDuration, String sponsorImageUrl, String sponsorWebsiteUrl,
            String sponsorClass) {
        Query query = new Query(Criteria.where("sponsorName").is(name));
        Update update = new Update();

        if (coopDuration != null && !coopDuration.isEmpty()) {
            update.set("coopDuration", coopDuration);
        }

        if (sponsorImageUrl != null && !sponsorImageUrl.isEmpty()) {
            update.set("sponsorImageUrl", sponsorImageUrl);
        }

        if (sponsorWebsiteUrl != null && !sponsorWebsiteUrl.isEmpty()) {
            update.set("sponsorWebsiteUrl", sponsorWebsiteUrl);
        }

        if (sponsorClass != null && !sponsorClass.isEmpty()) {
            update.set("sponsorClass", sponsorClass);
        }

        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Sponsor.class);
        return updateResult.wasAcknowledged() && updateResult.getModifiedCount() > 0;
    }

}
