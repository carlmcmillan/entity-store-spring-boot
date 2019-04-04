package es.entitystore;

import es.entitystore.messages.EntityStoreRequest;
import es.entitystore.messages.EntityStoreResponse;
import es.entitystore.model.Entity;
import es.entitystore.repository.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping(value = "/entities")
@RestController
public class EntityStoreController {

    @Autowired
    private EntityRepository entityRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EntityStoreResponse> getWithRemoteIds(@RequestParam(required = false, value = "remoteId1") String remoteId1, @RequestParam(required = false) String remoteId2) {
        EntityStoreResponse response = new EntityStoreResponse();

        List<Entity> retreivedEntities;
        if (StringUtils.hasText(remoteId1) && StringUtils.hasText(remoteId2)) {
            retreivedEntities = new ArrayList<>();
            Entity entity = entityRepository.findByRemoteIds(remoteId1, remoteId2);
            if (entity != null) {
                retreivedEntities.add(entity);
            }
        } else if (StringUtils.hasText(remoteId1)) {
            retreivedEntities = entityRepository.listRemoteId1(remoteId1);
        } else if (StringUtils.hasText(remoteId2)) {
            retreivedEntities = entityRepository.listRemoteId2(remoteId2);
        } else {
            retreivedEntities = entityRepository.all();
        }

        response.setEntities(retreivedEntities);
        return new ResponseEntity<EntityStoreResponse>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<EntityStoreResponse> getWithId(@PathVariable(required = false) String id) {
        EntityStoreResponse response = new EntityStoreResponse();

        List<Entity> retreivedEntities = new ArrayList<>();
        if (StringUtils.hasText(id)) {

            Entity entity = entityRepository.find(id);
            if (entity != null) {
                retreivedEntities.add(entity);
            }
        }

        response.setEntities(retreivedEntities);
        return new ResponseEntity<EntityStoreResponse>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ResponseEntity<EntityStoreResponse> getActive() {
        EntityStoreResponse response = new EntityStoreResponse();

        List<Entity> retreivedEntities = entityRepository.active();

        response.setEntities(retreivedEntities);
        return new ResponseEntity<EntityStoreResponse>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/inactive", method = RequestMethod.GET)
    public ResponseEntity<EntityStoreResponse> getInactive() {
        EntityStoreResponse response = new EntityStoreResponse();

        List<Entity> retreivedEntities = entityRepository.inactive();

        response.setEntities(retreivedEntities);
        return new ResponseEntity<EntityStoreResponse>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public EntityStoreResponse saveEntity(@RequestBody EntityStoreRequest request) {
        EntityStoreResponse response = new EntityStoreResponse();
        List<Entity> entities = new ArrayList();
        response.setEntities(entities);

        if (request.getEntity() != null) {
            entities.add(entityRepository.save(request.getEntity()));
        }
        return response;
    }

}
