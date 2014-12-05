package de.nixis.bpmn.repository.core.api;

import de.nixis.bpmn.repository.core.api.ModelResource;
import de.nixis.bpmn.repository.core.model.CreateModelData;
import de.nixis.bpmn.repository.core.model.ModelData;
import de.nixis.bpmn.repository.db.ModelDAO;
import de.nixis.bpmn.repository.db.entity.Model;
import de.nixis.bpmn.repository.service.ModelService;
import io.dropwizard.testing.junit.ResourceTestRule;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author nikku
 */
public class ModelResourceTest {

  private final ModelDAO modelDao = mock(ModelDAO.class);
  private final ModelService modelService = new ModelService();

  @Rule
  public final ResourceTestRule resources = ResourceTestRule.builder()
          .addResource(new ModelResource(modelService, modelDao))
          .build();

  
  private final Model model = new Model("XXX_KEY", "XXX", 0, "XXX", "FOO", null);
  
  
  @Before
  public void setup() {
  }

  @Test
  public void get() {
    
    // assume
    when(modelDao.findLatestById("XXX")).thenReturn(model);
    
    // when
    Response response = resources.client().target("/model").path("/XXX").request().get();
    ModelData modelData = response.readEntity(ModelData.class);
    
    int status = response.getStatus();
    System.out.println(status);
    
    // then
    assertThat(modelData.getId()).isEqualTo(model.getId());
    assertThat(modelData.getContent()).isEqualTo(model.getContent());
    assertThat(modelData.getType()).isEqualTo(model.getType());
    
    // linked
    Link selfLink = response.getLink("self");
    
    assertThat(selfLink).isNotNull();
    assertThat(selfLink.getUri().toString()).isEqualTo("/model/XXX");
    
    
    Link rawLink = response.getLink("raw");
    
    assertThat(rawLink).isNotNull();
    assertThat(rawLink.getUri().toString()).isEqualTo("/model/XXX/raw?rev=0");
  }
  
  @Test
  public void create() {
    
    // assume
    when(modelDao.create(any(Model.class))).thenAnswer((invocation) -> {
      return model;
    });
    
    CreateModelData createData = new CreateModelData("bpmn20");
    
    // when
    Response response = resources.client().target("/model/_initial").request().post(Entity.entity(createData, MediaType.APPLICATION_JSON));
    ModelData modelData = response.readEntity(ModelData.class);
    
    // then
    assertThat(modelData.getType()).isEqualTo(model.getType());
    assertThat(modelData.getContent()).isEqualTo(model.getContent());
    assertThat(modelData.getId()).isEqualTo(model.getId());
    assertThat(modelData.getRevision()).isEqualTo(0);
  }

  @Test
  public void createInvalid() {
    
    // when
    try {
      resources.client().target("/model/_initial").request().post(Entity.json(new CreateModelData()));
      
      fail("Expected validation error");
    
    } catch (ProcessingException e) {
      // then
      Throwable cause = e.getCause();
      
      assertThat(cause).isInstanceOf(ConstraintViolationException.class);
      assertThat(((ConstraintViolationException) cause).getConstraintViolations()).hasSize(1);
    }
  }

}
