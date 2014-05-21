package edu.tugraz.sw14.xp04.server;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;

import java.util.concurrent.ExecutionException;

import org.easymock.EasyMock;

import android.test.InstrumentationTestCase;
import edu.tugraz.sw14.xp04.server.LoginTask.LoginTaskListener;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;

public class LoginTaskTest extends InstrumentationTestCase {

  ServerConnection connectionMock;

  LoginTaskListener loginTaskListener = new LoginTaskListener() {
    @Override
    public void onPreExecute() {
    }

    @Override
    public void onPostExecute(LoginResponse response) {
    }
  };

  public LoginTaskTest() {
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
    String gmcId = "aabbccddeeffgghh";
    String email = "test@example.org";
    String password = "topsecret";

    LoginRequest request = new LoginRequest();
    request.setGcmId(gmcId);
    request.setId(email);
    request.setPassword(password);

    expect(connectionMock.login(request)).andReturn(null);
    EasyMock.replay(connectionMock);

    LoginTask loginTask = new LoginTask(connectionMock, loginTaskListener);
    loginTask.execute(request).get();

    EasyMock.verify(connectionMock);
  }
}
