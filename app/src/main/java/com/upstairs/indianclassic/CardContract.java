package com.upstairs.indianclassic;

import android.provider.BaseColumns;

/**
 * Created by karm on 24/6/16.
 */
public class CardContract {
    //empty constructor to prevent instantiation
    public CardContract(){}

    public static final String SHARED_PREFERENCES_KEY = "IndianGamesflags";
    public static final String DB_READY_FLAG= "DB_READY";
    public static abstract class CardTable implements BaseColumns {
        public static final String TABLE_NAME="cards";
        public static final String COLUMN_NAME_CARD_ID="cardid";
        public static final String COLUMN_NAME_CARD_TITLE="cardtitle";
        public static final String COLUMN_NAME_CARD_IMAGE="cardimage";
        public static final String COLUMN_NAME_CLASSIFICATION="classification";
        public static final String COLUMN_NAME_SUBCLASSIFICATION="subclassification";
        public static final String COLUMN_NAME_SUBSUBCLASSIFICATION="subsubclassification";
        public static final String COLUMN_NAME_CARD_TEXT="cardtext";
        public static final String COLUMN_NAME_CARD_LIST="cardlist";
        public static final String COLUMN_NAME_CARD_POSITION="pos";
    }
}
