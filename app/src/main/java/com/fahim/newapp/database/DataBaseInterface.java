package com.fahim.newapp.database;

/**
 * Created by Vaisakh on 12/9/2015.
 */

public interface DataBaseInterface {

    final String _ID = "_Id";


    final String TABLE_STANDARD_HOLDER = "standard";
    final String TABLE_SUBJECT_HOLDER = "subject";
    final String TABLE_BOOKS_HOLDER = "books";
    final String TABLE_FAV_HOLDER = "favourite";


    final String STANDARD_ID = "id";
    final String STANDARD_NAME = "standard_name";

    final String SUBJECT_ID = "id";
    final String SUBJECT_STANDARD_ID = "standard_id";
    final String SUBJECT_NAME = "subject_name";

    final String BOOKS_ID = "id";
    final String BOOKS_STANDARD_ID = "standard_id";
    final String Books_SUBJECT_ID = "subject_id";
    final String BOOKS_NAME = "book_name";
    final String BOOKS_LINK = "books_Links";
    final String BOOKS_VIEW_COUNT = "view_count";

    final String FAV_BOOKS_ID = "id";
    final String NOVELS = "NOVELS";


}
