package com.example.owner;
public class Constants {
   // public static final String ACTION_BOOT_COMPLETED = "com.coderperf.getback.BOOT_COMPLETED";
    public static final String ACTION_SMS_RECEIVED = "com.coderperf.getback.SMS_RECEIVED";
    public static final String ACTION_NETWORKSTATE_CHANGED = "com.coderperf.getback.NETWORK_STATE_CHANGED";
    public static final String ACTION_TEST = "com.coderperf.getback.test";
    public static final String ACTION_CLEAR_GETBACK = "com.coderperf.getback.clearstates";

    public static final String PREFERENCE_SIM_SERIAL = "Pref_Sim_Serial";
    public static final String PREFERENCE_EMAIL_NOTIF_FLAG = "Pref_Email_Notif_Flag";

    public static final String PREFERENCE_IS_THEFT_FLAG = "Pref_Is_Theft";
    public static final String PREFERENCE_IS_PHOTO_CAPTURED = "Pref_Is_Photo_Captured";
    public static final String PREFERENCE_IS_EMAIL_SENT = "Pref_Is_Email_Sent";
    public static final String PREFERENCE_IS_DATA_DELETED = "Pref_Is_Data_Deleted";
    public static final String PREFERENCE_IS_SMS_SENT = "Pref_Is_SMS_Sent";
    public static final String PREFERENCE_TRIGGER_ORIGIN = "pref_prigger_origin";
    public static final String PREFERENCE_TRIGGER_COMMAND = "pref_trigger_command";
    public static final String PREFERENCE_PHOTO_PATH = "pref_photo_path";

    public static final String KEY_SENDER = "Sender_Phone_Number";
    public static final String KEY_MESSAGE_BODY = "Sender_SMS_Body";
    public static final String KEY_PHOTO_PATH = "key_photo_path";
    public static final String KEY_CURRENT_ADDRESS = "key_current_address";

    public static final String DEFAULT_SENDER_EMAILADDRESS = "getback.codeperf@gmail.com";
    public static final String DEFAULT_SENDER_PASSWORD = "flhyjuoyaclmqvbg";

    public static final String BACKUP_CONTACT_FILE = "getback_contacts.vcf";
    public static final String SMS_COMMAND_DELIMETER = "::";
    public static final String LOG_TAG = "GetBack";
    public static final boolean DEBUG_FLAG = false;
}