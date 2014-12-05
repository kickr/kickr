package de.nixis.bpmn.repository.core.api;

import de.nixis.bpmn.repository.service.TypeInfo;
import de.nixis.bpmn.repository.core.model.CreateModelData;
import de.nixis.bpmn.repository.core.model.ModelData;
import de.nixis.bpmn.repository.db.ModelDAO;
import de.nixis.bpmn.repository.db.entity.Model;
import de.nixis.bpmn.repository.service.ModelService;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import javax.ws.rs.core.Response.Status;

/**
 *
 * @author nikku
 */
@Path("/model")
@Produces(MediaType.APPLICATION_JSON)
public class ModelResource {
  
  private final ModelService modelService;

  private final ModelDAO modelDao;
  
  
  public ModelResource(ModelService modelService, ModelDAO modelDao) {
    this.modelService = modelService;
    this.modelDao = modelDao;
  }
  
  
  // API
  
  @GET
  @UnitOfWork
  public Response list() {
    UriBuilder uriBuilder = UriBuilder.fromResource(this.getClass());
            
    List<Model> models = modelDao.query();
    
    ModelData[] data = models.stream().map(ModelData::fromModel).toArray(ModelData[]::new);
    
    return Response
             .ok(data)
               .build();
  }
  
  @POST
  @Path("_initial")
  @UnitOfWork
  public Response createInitial(@Valid @NotNull CreateModelData data) {
    
    String type = data.getType();
    
    Model model;
    
    try {
      model = modelService.initializeModel(type);
    } catch (Exception e) {
      throw new WebApplicationException(e, Status.BAD_REQUEST);
    }

    model = modelDao.create(model);
    
    return linked(model);
  }
  
  @GET
  @Path("{id}")
  @UnitOfWork
  public Response get(@PathParam("id") String id, @QueryParam("rev") @DefaultValue("-1") long revision) {
    Model model;
    
    if (revision == -1) {
      model = modelDao.findLatestById(id);
    } else {
      model = modelDao.findByRevision(id, revision);
    }

    if (model == null) {
      throw notFound();
    }
    
    return linked(model);
  }
  
  @GET
  @Path("{id}/download")
  @UnitOfWork
  public Response download(@PathParam("id") String id, @QueryParam("rev") long revision) {
    Model model = modelDao.findByRevision(id, revision);

    if (model == null) {
      throw notFound();
    }
    
    TypeInfo info = modelService.getTypeInfo(model);
    
    return Response
              .ok(model.getContent())
              .header("Content-Disposition", "attachment; filename=" + id + "." + info.getExtension())
              .header("Content-Type", info.getContentType())
              .build();
  }
  
  @GET
  @Path("{id}/raw")
  @UnitOfWork
  public Response getRaw(@PathParam("id") String id, @QueryParam("rev") long revision) {
    Model model = modelDao.findByRevision(id, revision);

    if (model == null) {
      throw notFound();
    }
    
    return Response.ok(model.getContent()).build();
  }
  
  @PUT
  @Path("{id}")
  @UnitOfWork
  public Response update(@PathParam("id") String id, @Valid ModelData data) {
    
    if (!id.equals(data.getId())) {
      throw new WebApplicationException(Status.BAD_REQUEST);
    }
    
    Model model = modelDao.findLatestById(id);
    
    if (model == null) {
      throw notFound();
    }
    
    if (!model.getType().equals(data.getType())) {
      throw error("Must not change type", Status.BAD_REQUEST);
    }
    
    if (model.getRevision() != data.getRevision()) {
      throw error("Conflicting updates", Status.CONFLICT);
    }
    
    Model newModel = modelService.updateModel(model, data.getContent());
    
    newModel = modelDao.create(newModel);

    return linked(newModel);
  }

  
  // HELPERS
  
  private Response linked(Model model) {
    
    ModelData data = ModelData.fromModel(model);
    
    UriBuilder modelUriBuilder = UriBuilder.fromResource(this.getClass());
    
    return Response
      .ok(data)
      .link(modelUriBuilder.path("/{id}").build(data.getId()), "self")
      .link(modelUriBuilder.path("/raw{?rev}").build(data.getId(), data.getRevision()), "raw")
      .build();
  }

  private WebApplicationException error(String message, Status status) {
    return new WebApplicationException(message, status);
  }

  private WebApplicationException notFound() {
    return error("Not found", Status.NOT_FOUND);
  }
}
