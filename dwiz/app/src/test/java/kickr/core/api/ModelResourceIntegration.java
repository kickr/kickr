package kickr.core.api;

import kickr.RepositoryApplication;
import kickr.RepositoryConfiguration;
import kickr.core.model.CreateModelData;
import kickr.core.model.ModelData;
import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import io.dropwizard.testing.junit.DropwizardAppRule;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 * @author nikku
 */
public class ModelResourceIntegration {

  private static final String START_EVENT = "<bpmn:startEvent id=\"StartEvent_1\"/>";

  
  @Rule
  public final DropwizardAppRule<RepositoryConfiguration> RULE
                  = new DropwizardAppRule<>(RepositoryApplication.class, resourceFilePath("repository-test.yml"));

  private WebTarget api;
  
  @Before
  public void before() {
    Client client = ClientBuilder.newClient();
    api = client.target(String.format("http://localhost:%d/api/", RULE.getLocalPort()));
  }
  
  @Test
  public void list() {
    Response response = api.path("/model").request().get();
    
    // ok
    assertThat(response.getStatus()).isEqualTo(200);
  }
  
  @Test
  public void create() {
    
    // create
    ModelData created = createModel();

    Response getResponse = api.path("/model/{id}").resolveTemplate("id", created.getId()).request().get();
    ModelData getModel = getResponse.readEntity(ModelData.class);
    
    // expect
    assertThat(getModel.getId()).isEqualTo(created.getId());
    assertThat(getModel.getRevision()).isEqualTo(created.getRevision());
    assertThat(getModel.getContent()).isEqualTo(created.getContent());
    
    assertThat(getModel.getContent()).contains(START_EVENT);
  }
  
  @Test
  public void getRaw() {
    
    // create
    ModelData model = createModel();
    
    // get raw
    Map<String, Object> params = new HashMap<>();
    params.put("id", model.getId());
    params.put("rev", model.getRevision());
    
    Response rawResponse = api.path("/model/{id}/raw{?rev}").resolveTemplates(params).request().get();
    
    String contents = rawResponse.readEntity(String.class);
    
    // expect
    assertThat(contents).contains(START_EVENT);
  }
  
  @Test
  public void get_notFound() {
    Response response = api.path("/model/not-found").request().get();
    assertThat(response.getStatus()).isEqualTo(404);
  }
  
  @Test
  public void update() {
    
    // create
    ModelData model = createModel();

    // local update
    model.setContent("XXX");
    
    // save
    Response updateResponse = api.path("/model/{id}").resolveTemplate("id", model.getId()).request().put(Entity.entity(model, MediaType.APPLICATION_JSON));
    ModelData updatedModel = updateResponse.readEntity(ModelData.class);

    // expect
    // we receive an updated model with a new revision
    assertThat(updatedModel.getId()).isEqualTo(model.getId());
    assertThat(updatedModel.getRevision()).isEqualTo(model.getRevision() + 1);
    
    assertThat(updatedModel.getContent()).isEqualTo("XXX");
  }
  
  @Test
  public void update_notFound() {

    // update
    ModelData updateModel = new ModelData("not-found", 0, "CONTENT", "XXX");
    
    // save
    Response updateResponse = api.path("/model/{id}").resolveTemplate("id", updateModel.getId()).request().put(Entity.entity(updateModel, MediaType.APPLICATION_JSON));

    // expect
    assertThat(updateResponse.getStatus()).isEqualTo(404);
  }
  
    
  @Test
  public void update_conflict() {

    // update
    ModelData model = createModel();
    
    // first update
    api.path("/model/{id}").resolveTemplate("id", model.getId()).request().put(Entity.entity(model, MediaType.APPLICATION_JSON));

    // when
    // conflicting update
    model.setContent("XXX");

    Response conflictingResponse = api.path("/model/{id}").resolveTemplate("id", model.getId()).request().put(Entity.entity(model, MediaType.APPLICATION_JSON));

    
    // expect
    assertThat(conflictingResponse.getStatus()).isEqualTo(409);
  }

  private ModelData createModel() {

    CreateModelData createData = new CreateModelData("bpmn20");
    Response createResponse = api.path("/model/_initial").request().post(Entity.entity(createData, MediaType.APPLICATION_JSON));
    
    return createResponse.readEntity(ModelData.class);
  }
}
