package edu.tugraz.sw14.xp04.database;

import java.util.ArrayList;

import edu.tugraz.sw14.xp04.contacts.Contact;
import edu.tugraz.sw14.xp04.helpers.ChatOverview;
import edu.tugraz.sw14.xp04.msg.Msg;
import android.content.ContentValues;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class DatabaseTest extends AndroidTestCase {
	private Database db;

	private static final String nll = null;
	private static final String name = "Hauns";
	private static final String mail = "mail";
	private static final String content = "this is a msg";
	private static final long timestamp = 1;
	private static final Msg msg = new Msg(mail, content, timestamp, true, true);

	public void setUp() {
		RenamingDelegatingContext context = new RenamingDelegatingContext(
				getContext(), "test_");
		db = new Database(context);
	}

	public void testGetAllContactsListEmpty() {
		ArrayList<Contact> list = db.getAllContacts();
		assertEquals(0, list.size());
	}

	public void testGetAllContacts() {
		Contact contact1 = new Contact(name, mail, null);
		Contact contact2 = new Contact("Hans", "mail1", null);
		Contact contact3 = new Contact("Franz", "mail2", null);
		db.insertContact(contact1.toContentValues());
		db.insertContact(contact2.toContentValues());
		db.insertContact(contact3.toContentValues());
		ArrayList<Contact> list = db.getAllContacts();
		assertEquals(3, list.size());
	}

	public void testInsertContact() {
		Contact contact = new Contact(name, mail, null);
		boolean result = db.insertContact(contact.toContentValues());
		assertEquals(true, result);
		ArrayList<Contact> list = db.getAllContacts();
		assertEquals(1, list.size());
	}

	public void testInsertContactEquals() {
		Contact contact = new Contact(name, mail, null);
		boolean result = db.insertContact(contact.toContentValues());
		assertEquals(true, result);
		ArrayList<Contact> list = db.getAllContacts();
		Contact retContact = list.get(0);
		assertEquals(true, contact.equals(retContact));
	}

	public void testInsertContactWithNull() {
		boolean result = db.insertContact(null);
		assertEquals(false, result);
		ArrayList<Contact> list = db.getAllContacts();
		assertEquals(0, list.size());
	}

	public void testInsertContactWithNameIsNull() {
		Contact contact = new Contact(null, mail, null);
		boolean result = db.insertContact(contact.toContentValues());
		assertEquals(false, result);
		ArrayList<Contact> list = db.getAllContacts();
		assertEquals(0, list.size());
	}

	public void testInsertContactWithEmailIsNull() {
		Contact contact = new Contact(name, null, null);
		boolean result = db.insertContact(contact.toContentValues());
		assertEquals(false, result);
		ArrayList<Contact> list = db.getAllContacts();
		assertEquals(0, list.size());
	}

	public void testInsertContactWithCorruptContentValue1() {
		ContentValues v = new ContentValues();
		v.put(Database.CONTACT_USR_ID, nll);
		v.put(Database.CONTACT_NAME, mail);
		v.put(Database.CONTACT_IMG_URL, nll);
		boolean result = db.insertContact(v);
		assertEquals(false, result);
		ArrayList<Contact> list = db.getAllContacts();
		assertEquals(0, list.size());
	}

	public void testInsertContactWithCorruptContentValue2() {
		ContentValues v = new ContentValues();
		v.put(Database.CONTACT_USR_ID, name);
		v.put(Database.CONTACT_NAME, nll);
		v.put(Database.CONTACT_IMG_URL, nll);
		boolean result = db.insertContact(v);
		assertEquals(false, result);
		ArrayList<Contact> list = db.getAllContacts();
		assertEquals(0, list.size());
	}

	public void testInsertContactDoubleEntry() {
		Contact contact = new Contact(name, mail, null);
		boolean result = db.insertContact(contact.toContentValues());
		assertEquals(true, result);
		result = db.insertContact(contact.toContentValues());
		assertEquals(false, result);
		ArrayList<Contact> list = db.getAllContacts();
		assertEquals(1, list.size());
	}

	public void testGetContactId() {
		Contact contact = new Contact(name, mail, null);
		boolean result = db.insertContact(contact.toContentValues());
		assertEquals(true, result);
		int id = db.getContactId(mail);
		assertEquals(1, id);
	}

	public void testGetContactIdEmailNull() {
		Contact contact = new Contact(name, mail, null);
		boolean result = db.insertContact(contact.toContentValues());
		assertEquals(true, result);
		int id = db.getContactId(nll);
		assertEquals(-1, id);
	}

	public void testGetContactIdListEmpty() {
		int id = db.getContactId(mail);
		assertEquals(-1, id);
	}

	public void testGetContactIdListEmptyEmailNull() {
		int id = db.getContactId(nll);
		assertEquals(-1, id);
	}

	public void testGetContactName() {
		Contact contact = new Contact(name, mail, null);
		boolean result = db.insertContact(contact.toContentValues());
		assertEquals(true, result);
		String retName = db.getContactName(mail);
		assertEquals(name, retName);
	}

	public void testGetContactNameEmailIsNull() {
		Contact contact = new Contact(name, mail, null);
		boolean result = db.insertContact(contact.toContentValues());
		assertEquals(true, result);
		String retName = db.getContactName(nll);
		assertEquals(nll, retName);
	}

	public void testGetContactNameListEmpty() {
		String retName = db.getContactName(mail);
		assertEquals(nll, retName);
	}

	public void testGetContactNameListEmptyEmailIsNull() {
		String retName = db.getContactName(nll);
		assertEquals(nll, retName);
	}

	public void testContactAlreadyExistsTrue() {
		Contact contact = new Contact(name, mail, null);
		boolean result = db.insertContact(contact.toContentValues());
		assertEquals(true, result);
		boolean ret = db.contactAlreadyExists(mail);
		assertEquals(true, ret);
	}

	public void testContactAlreadyExistsFalse() {
		boolean ret = db.contactAlreadyExists(mail);
		assertEquals(false, ret);
	}

	public void testContactAlreadyExistsEmailNull() {
		Contact contact = new Contact(name, mail, null);
		boolean result = db.insertContact(contact.toContentValues());
		assertEquals(true, result);
		boolean ret = db.contactAlreadyExists(nll);
		assertEquals(false, ret);
	}

	public void testContactAlreadyListEmpty() {
		boolean ret = db.contactAlreadyExists(mail);
		assertEquals(false, ret);
	}

	public void testContactAlreadyExistsListEmptyEmailNull() {
		boolean ret = db.contactAlreadyExists(nll);
		assertEquals(false, ret);
	}

	public void testInsertMsg() {
		boolean result = db.insertMsg(msg.toContentValues());
		assertEquals(true, result);
		ArrayList<Msg> list = db.getMsgsBySender(mail, 0);
		assertEquals(1, list.size());
	}

	public void testInsertMsgEquals() {
		boolean result = db.insertMsg(msg.toContentValues());
		assertEquals(true, result);
		ArrayList<Msg> list = db.getMsgsBySender(mail, 0);
		Msg retMsg = list.get(0);
		assertEquals(true, msg.equals(retMsg));
	}

	public void testInsertMsgWithNull() {
		boolean result = db.insertMsg(null);
		assertEquals(false, result);
		ArrayList<Msg> list = db.getMsgsBySender(mail, 0);
		assertEquals(0, list.size());
	}

	public void testInsertMsgWithEmailIsNull() {
		Msg msg = new Msg(nll, content, timestamp, true, true);
		boolean result = db.insertMsg(msg.toContentValues());
		assertEquals(false, result);
	}

	public void testInsertMsgWithContentIsNull() {
		Msg msg = new Msg(mail, nll, timestamp, true, true);
		boolean result = db.insertMsg(msg.toContentValues());
		assertEquals(false, result);
	}

	public void testInsertMsgWithCorruptContentValue1() {
		ContentValues v = new ContentValues();
		v.put(Database.MSG_SENDER_ID, nll);
		v.put(Database.MSG_CONTENT, content);
		v.put(Database.MSG_TIMESTAMP, timestamp);
		v.put(Database.MSG_FLAG_OWN, true);
		v.put(Database.MSG_FLAG_READ, true);
		boolean result = db.insertMsg(v);
		assertEquals(false, result);
	}

	public void testInsertMsgWithCorruptContentValue2() {
		ContentValues v = new ContentValues();
		v.put(Database.MSG_SENDER_ID, mail);
		v.put(Database.MSG_CONTENT, nll);
		v.put(Database.MSG_TIMESTAMP, timestamp);
		v.put(Database.MSG_FLAG_OWN, true);
		v.put(Database.MSG_FLAG_READ, true);
		boolean result = db.insertMsg(v);
		assertEquals(false, result);
		ArrayList<Msg> list = db.getMsgsBySender(mail, 0);
		assertEquals(0, list.size());
	}

	public void testGetMsgBySenderListEmpty() {
		ArrayList<Msg> list = db.getMsgsBySender(mail, 0);
		assertEquals(0, list.size());
	}

	public void testGetMsgBySenderEmailIsNull() {
		ArrayList<Msg> list = db.getMsgsBySender(nll, 0);
		assertEquals(0, list.size());
	}

	public void testGetMsgBySenderDifferentSenders() {
		Msg msg1 = new Msg(mail, content, timestamp, true, true);
		boolean result = db.insertMsg(msg1.toContentValues());
		assertEquals(true, result);
		Msg msg2 = new Msg("mail2", content, timestamp, true, true);
		result = db.insertMsg(msg2.toContentValues());
		assertEquals(true, result);
		ArrayList<Msg> list = db.getMsgsBySender(mail, 0);
		assertEquals(1, list.size());
	}

	public void testGetMsgBySenderLimit() {
		Msg msg1 = new Msg(mail, content, timestamp, true, true);
		boolean result = db.insertMsg(msg1.toContentValues());
		assertEquals(true, result);
		Msg msg2 = new Msg(mail, content, timestamp, true, true);
		result = db.insertMsg(msg2.toContentValues());
		assertEquals(true, result);
		ArrayList<Msg> list = db.getMsgsBySender(mail, 1);
		assertEquals(1, list.size());
	}

	public void testGetMsgBySender() {
		Msg msg1 = new Msg(mail, content, timestamp, true, true);
		boolean result = db.insertMsg(msg1.toContentValues());
		assertEquals(true, result);
		Msg msg2 = new Msg(mail, content, timestamp, true, true);
		result = db.insertMsg(msg2.toContentValues());
		assertEquals(true, result);
		ArrayList<Msg> list = db.getMsgsBySender(mail, 0);
		assertEquals(2, list.size());
	}

	public void testGetAllMsgsListEmpty() {
		ArrayList<ChatOverview> list = db.getAllMsgs(0);
		assertEquals(0, list.size());
	}

	public void testGetAllMsgs() {
		Msg msg1 = new Msg(mail, content, timestamp, true, true);
		boolean result = db.insertMsg(msg1.toContentValues());
		assertEquals(true, result);
		Msg msg2 = new Msg("mail2", content, timestamp, true, true);
		result = db.insertMsg(msg2.toContentValues());
		assertEquals(true, result);
		ArrayList<ChatOverview> list = db.getAllMsgs(0);
		assertEquals(2, list.size());
	}

	public void testGetAllMsgsEqualSender() {
		Msg msg1 = new Msg(mail, content, timestamp, true, true);
		boolean result = db.insertMsg(msg1.toContentValues());
		assertEquals(true, result);
		Msg msg2 = new Msg(mail, content, timestamp, true, true);
		result = db.insertMsg(msg2.toContentValues());
		assertEquals(true, result);
		ArrayList<ChatOverview> list = db.getAllMsgs(0);
		assertEquals(1, list.size());
	}

	public void testGetAllMsgsLimit() {
		Msg msg1 = new Msg(mail, content, timestamp, true, true);
		boolean result = db.insertMsg(msg1.toContentValues());
		assertEquals(true, result);
		Msg msg2 = new Msg("mail2", content, timestamp, true, true);
		result = db.insertMsg(msg2.toContentValues());
		assertEquals(true, result);
		ArrayList<ChatOverview> list = db.getAllMsgs(1);
		assertEquals(1, list.size());
	}

	public void tearDown() throws Exception {
		db.close();
		super.tearDown();
	}
}
