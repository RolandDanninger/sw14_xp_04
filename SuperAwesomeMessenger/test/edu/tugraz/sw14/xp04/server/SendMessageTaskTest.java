package edu.tugraz.sw14.xp04.server;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;

import java.util.concurrent.ExecutionException;

import org.easymock.EasyMock;

import android.test.InstrumentationTestCase;
import edu.tugraz.sw14.xp04.server.SendMessageTask.SendMessageTaskListener;
import edu.tugraz.sw14.xp04.stubs.SendMessageRequest;
import edu.tugraz.sw14.xp04.stubs.SendMessageResponse;

public class SendMessageTaskTest extends InstrumentationTestCase {

  ServerConnection connectionMock;

  private SendMessageTaskListener sendMessageTaskListener = new SendMessageTaskListener() {

    @Override
    public void onPreExecute() {
    }

    @Override
    public void onPostExecute(SendMessageResponse response) {
    }
  };

  public SendMessageTaskTest() {
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
    String id = "test@example.org";
    String msg = "i am a test message";

    SendMessageRequest request = new SendMessageRequest();
    request.setReceiverId(id);
    request.setMessage(msg);

    expect(connectionMock.sendMessage(request)).andReturn(null);
    EasyMock.replay(connectionMock);

    SendMessageTask SendMessageTask = new SendMessageTask(connectionMock, sendMessageTaskListener);
    SendMessageTask.execute(request).get();

    EasyMock.verify(connectionMock);
  }
}
