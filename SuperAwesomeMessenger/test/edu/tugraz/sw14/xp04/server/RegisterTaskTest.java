package edu.tugraz.sw14.xp04.server;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;

import java.util.concurrent.ExecutionException;

import org.easymock.EasyMock;

import android.test.InstrumentationTestCase;
import edu.tugraz.sw14.xp04.server.RegistrationTask.RegistrationTaskListener;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;
import edu.tugraz.sw14.xp04.stubs.RegistrationResponse;

public class RegisterTaskTest extends InstrumentationTestCase {

  ServerConnection connectionMock;

  RegistrationTaskListener registrationTaskListener = new RegistrationTaskListener() {
    @Override
    public void onPostExecute(RegistrationResponse response) {
    }
  };

  public RegisterTaskTest() {
    super();
  }

  @Override
  protected void setUp() throws Exception {

    System.setProperty("dexmaker.dexcache", getInstrumentation()
        .getTargetContext().getCacheDir().getPath());

    connectionMock = createMock(ServerConnection.class);
  }

  @Override
  protected void tearDown() throws Exception {
  }

  public void testExecute() throws ServerConnectionException,
      InterruptedException, ExecutionException {

    RegistrationRequest request = new RegistrationRequest();
    request.setId("aabbcc");
    request.setName("Max Mustermann");
    request.setPassword("topsecret");

    expect(connectionMock.register(request)).andReturn(null);
    EasyMock.replay(connectionMock);

    RegistrationTask registrationTask = new RegistrationTask(connectionMock,
        this.registrationTaskListener);
    registrationTask.execute(request).get();

    EasyMock.verify(connectionMock);
  }
}
