package com.andrewchelladurai.simplebible.data;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.dao.BookDao;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;
import com.andrewchelladurai.simplebible.data.entity.Book;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import com.andrewchelladurai.simplebible.utils.BookUtils;
import com.andrewchelladurai.simplebible.utils.VerseUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DbSetupJob
    extends JobIntentService {

  public static final int STARTED = 0;
  public static final int RUNNING = STARTED + 1;
  public static final int FAILED = RUNNING + 1;
  public static final int FINISHED = FAILED + 1;

  private static final String TAG = "DbSetupJob";
  private static final int JOB_ID = 131416;
  private static ResultReceiver RESULT_RECEIVER;

  public static void startWork(@NonNull Context context,
                               @NonNull Intent work,
                               @NonNull final ResultReceiver resultReceiver) {
    RESULT_RECEIVER = resultReceiver;
    enqueueWork(context, DbSetupJob.class, JOB_ID, work);
  }

  @Override
  protected void onHandleWork(@NonNull final Intent intent) {
    Log.d(TAG, "onHandleWork");
    RESULT_RECEIVER.send(STARTED, Bundle.EMPTY);
    startForegroundNotification();

    final BookDao bookDao = SbDatabase.getDatabase(getApplication()).getBookDao();
    // check if contents of books table is valid*/
    if (!validateBooksTable(bookDao)) {
      // if not then populate the table
      if (!populateBooksTable(bookDao)) {
        // if population of table fails, broadcast failure
        RESULT_RECEIVER.send(FAILED, Bundle.EMPTY);
        return;
      } else {
        Log.d(TAG, "onHandleWork: now bookCount[" + bookDao.getBookCount()
                   + "] && expectedCount[" + BookUtils.EXPECTED_COUNT + "]");
      }
    }

    final VerseDao verseDao = SbDatabase.getDatabase(getApplication()).getVerseDao();
    // check if contents of verses table is valid*/
    if (!validateVersesTable(verseDao)) {
      // if not then populate the table
      if (!populateVersesTable(verseDao)) {
        // if population of table fails, broadcast failure
        RESULT_RECEIVER.send(FAILED, Bundle.EMPTY);
      } else {
        Log.d(TAG, "onHandleWork: now verseCount[" + verseDao.getVerseCount()
                   + "] && expectedCount[" + VerseUtils.EXPECTED_COUNT + "]");
      }
    }

    RESULT_RECEIVER.send(FINISHED, Bundle.EMPTY);
  }

  private void startForegroundNotification() {
    Log.d(TAG, "startForegroundNotification");
    final Intent intent = new Intent(this, DbSetupJob.class);
    final PendingIntent pendingIntent =
        PendingIntent.getActivity(this, 0, intent, 0);
    startForeground(JOB_ID, new NotificationCompat.Builder(this, getPackageName())
                                .setContentTitle(getString(R.string.dbSetupNotificationTitle))
                                .setContentText(getString(R.string.dbSetupNotificationMessage))
                                .setContentIntent(pendingIntent)
                                .setOngoing(true)
                                .setSmallIcon(R.drawable.ic_logo)
                                .setOnlyAlertOnce(true)
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .build());
  }

  /**
   * Fill the verses table in the database by reading contents of an asset file. @param verseDao used
   * to invoke the utility method of creating a verse record in the table. @return true if record is
   * successfully created, false otherwise.
   *
   * @param verseDao
   */
  private boolean populateVersesTable(@NonNull final VerseDao verseDao) {
    final String fileName = VerseUtils.SETUP_FILE;
    final String separator = VerseUtils.SETUP_FILE_RECORD_SEPARATOR;
    final int separatorCount = VerseUtils.SETUP_FILE_RECORD_SEPARATOR_COUNT;

    String[] parts;
    String translation;
    String text;
    int book;
    int chapter;
    int verse;

    try {
      final InputStream stream = getResources().getAssets().open(fileName);
      final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      String line;
      while (null != (line = reader.readLine())) {
        if (!line.contains(separator)) {
          Log.e(TAG, "populateVersesTable: skipping [" + line + "], no [" + separator + "] found");
          continue;
        }

        parts = line.split(separator);
        if (parts.length != separatorCount + 1) {
          Log.e(TAG, "populateVersesTable: skipping [" + line + "] " + "it does't have ["
                     + separatorCount + "] [" + separator + "]");
          continue;
        }

        translation = parts[0];
        text = parts[4];

        try {
          book = Integer.parseInt(parts[1]);
          chapter = Integer.parseInt(parts[2]);
          verse = Integer.parseInt(parts[3]);
        } catch (NumberFormatException nfe) {
          Log.e(TAG, "populateVersesTable: part of line [" + line + "]  is NAN", nfe);
          continue;
        }

        if (translation.isEmpty()) {
          Log.e(TAG, "populateVersesTable: empty translation in line [" + line + "]");
          continue;
        }

        if (text.isEmpty()) {
          Log.e(TAG, "populateVersesTable: empty text in line [" + line + "]");
          continue;
        }

        if (book < 1 || book > 66) {
          Log.e(TAG, "populateVersesTable: invalid book number in line [" + line + "]");
          continue;
        }

        if (chapter < 1) {
          Log.e(TAG, "populateVersesTable: invalid chapters count in line [" + line + "]");
          continue;
        }

        if (verse < 1) {
          Log.e(TAG, "populateVersesTable: invalid  verses count in line [" + line + "]");
          continue;
        }
        // using the values, create a new verse in the database.
        verseDao.createVerse(new Verse(translation, book, chapter, verse, text));
      }
    } catch (IOException e) {
      Log.e(TAG, "populateVersesTable: exception processing [" + fileName + "]", e);
      return false;
    }

    Log.d(TAG, "populateVersesTable: processed [" + fileName + "]");
    return true;
  }

  /**
   * Check the verses table to check if it's contents are valid. @param verseDao used for invoking
   * utility methods @return true if table meets expectations, false otherwise
   *
   * @param verseDao
   */
  private boolean validateVersesTable(@NonNull VerseDao verseDao) {
    final int expectedCount = VerseUtils.EXPECTED_COUNT;
    if (verseDao.getVerseCount() != expectedCount) {
      Log.e(TAG, "validateVersesTable: verseCount[" + verseDao.getVerseCount()
                 + "] != expectedCount[" + expectedCount + "]");
      return false;
    }

    Log.d(TAG, "validateVersesTable: [" + expectedCount + "] verses exist");
    return true;
  }

  /**
   * Fill the books table in the database by reading contents of an asset file. @param bookDao used
   * to invoke the utility method of creating a book record in the table. @return true if a record
   * is successfully created, false otherwise.
   */
  private boolean populateBooksTable(@NonNull final BookDao bookDao) {
    final String fileName = BookUtils.SETUP_FILE;
    final String separator = BookUtils.SETUP_FILE_RECORD_SEPARATOR;
    final int separatorCount = BookUtils.SETUP_FILE_RECORD_SEPARATOR_COUNT;

    String[] parts;
    String testament;
    String description;
    String name;
    int position;
    int chapters;
    int verses;

    try {
      final InputStream stream = getResources().getAssets().open(fileName);
      final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      String line;

      while (null != (line = reader.readLine())) {
        if (!line.contains(separator)) {
          Log.e(TAG, "populateBooksTable: skipping [" + line + "], no [" + separator + "] found");
          continue;
        }

        parts = line.split(separator);
        if (parts.length != separatorCount + 1) {
          Log.e(TAG,
                "populateBooksTable: skipping [" + line + "] " + "it does't have [" + separatorCount
                + "] [" + separator + "]");
          continue;
        }

        testament = parts[0];
        description = parts[1];
        name = parts[3];
        try {
          position = Integer.parseInt(parts[2]);
          chapters = Integer.parseInt(parts[4]);
          verses = Integer.parseInt(parts[5]);
        } catch (NumberFormatException nfe) {
          Log.e(TAG, "populateBooksTable: part of line [" + line + "] is NAN", nfe);
          continue;
        }

        if (testament.isEmpty()) {
          Log.e(TAG, "populateBooksTable: empty testament in line [" + line + "]");
          continue;
        }

        if (description.isEmpty()) {
          Log.e(TAG, "populateBooksTable: empty description in line [" + line + "]");
          continue;
        }

        if (name.isEmpty()) {
          Log.e(TAG, "populateBooksTable: empty name in line [" + line + "]");
          continue;
        }

        if (position < 1 || position > 66) {
          Log.e(TAG, "populateBooksTable: invalid book position in line [" + line + "]");
          continue;
        }

        if (chapters < 1) {
          Log.e(TAG, "populateBooksTable: invalid chapters count in line [" + line + "]");
          continue;
        }

        if (verses < 1) {
          Log.e(TAG, "populateBooksTable: invalid  verses count in line [" + line + "]");
          continue;
        }

        // using the data values, create a new book record in the database
        bookDao.createBook(new Book(testament, description, position, name, chapters, verses));
      }
    } catch (IOException e) {
      Log.e(TAG, "populateBooksTable: exception processing [" + fileName + "]", e);
      return false;
    }

    Log.d(TAG, "populateBooksTable: processed [" + fileName + "]");
    return true;
  }

  /**
   * Check the books table to check if it's contents are valid. @param utils used for invoking
   * utility methods @return true if table meets expectations, false otherwise
   */
  private boolean validateBooksTable(@NonNull final BookDao dao) {
    final int expectedCount = BookUtils.EXPECTED_COUNT;

    if (dao.getBookCount() != expectedCount) {
      Log.e(TAG, "validateBooksTable: bookCount[" + dao.getBookCount()
                 + "] != expectedCount[" + expectedCount + "]");
      return false;
    }
    Log.d(TAG, "validateBooksTable: [" + expectedCount + "] books exist");

    String expectedValue = getString(R.string.bookNameFirst);
    if (!dao.getBookUsingPosition(1).getName().equalsIgnoreCase(expectedValue)) {
      Log.e(TAG, "validateBooksTable: incorrect first book in db");
      return false;
    }
    Log.d(TAG, "validateBooksTable: book[" + 1 + "] name is [" + expectedValue + "]");

    expectedValue = getString(R.string.bookNameLast);
    if (!dao.getBookUsingPosition(expectedCount).getName().equalsIgnoreCase(expectedValue)) {
      Log.e(TAG, "validateBooksTable: incorrect last book in db");
      return false;
    }

    Log.d(TAG, "validateBooksTable: book[" + expectedCount + "] name is [" + expectedValue + "]");
    return true;
  }

}
