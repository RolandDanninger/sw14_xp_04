package edu.tugraz.sw14.xp04.server;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;

import java.util.concurrent.ExecutionException;

import org.easymock.EasyMock;

import android.test.InstrumentationTestCase;
import edu.tugraz.sw14.xp04.server.AddContactTask.AddContactTaskListener;
import edu.tugraz.sw14.xp04.stubs.AddContactRequest;
import edu.tugraz.sw14.xp04.stubs.AddContactResponse;


public class AddContactTaskTest extends InstrumentationTestCase {

  
  ServerConnection connectionMock;

  
  AddContactTaskListener addContactTaskListener = new AddContactTaskListener() {
    @Override
    public void onPreExecute() {
    }
    @Override
    public void onPostExecute(AddContactResponse response) {
    }
  };

  public AddContactTaskTest() {
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

    AddContactRequest request = new AddContactRequest();
    request.setId("1");

    expect(connectionMock.addContact(request)).andReturn(null);
    EasyMock.replay(connectionMock);

    AddContactTask addContactTask = new AddContactTask(connectionMock,
        addContactTaskListener);
    addContactTask.execute(request).get();

    EasyMock.verify(connectionMock);
  }
}
