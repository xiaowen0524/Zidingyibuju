package text.bwei.com.zidingyibuju;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.open).setOnClickListener(this);
        findViewById(R.id.close).setOnClickListener(this);
        createNotification();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.open:
                createNotification();
                break;
            case R.id.close:
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.cancel(5270);
                break;
            default:
                break;
        }
    }

    private void createNotification() {
        // BEGIN_INCLUDE(notificationCompat)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this);
        // END_INCLUDE(notificationCompat)

        // BEGIN_INCLUDE(intent)
        // Create Intent to launch this Activity again if the notification is
        // clicked.
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intent);
        // END_INCLUDE(intent)

        // BEGIN_INCLUDE(ticker)
        // Sets the ticker text
        builder.setTicker(getResources()
                .getString(R.string.custom_notification));

        // Sets the small icon for the ticker
        //通知栏未展开时显示的小图标
        builder.setSmallIcon(R.drawable.ic_stat_custom);
        // END_INCLUDE(ticker)

        // BEGIN_INCLUDE(buildNotification)
        // Cancel the notification when clicked
        builder.setAutoCancel(true);

        // Build the notification
        Notification notification = builder.build();
        // END_INCLUDE(buildNotification)
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
        notification.flags |= Notification.FLAG_NO_CLEAR;
        // BEGIN_INCLUDE(customLayout)
        // Inflate the notification layout as RemoteViews
        RemoteViews contentView = new RemoteViews(getPackageName(),
                R.layout.notifycation_layout);
        Intent iintent = new Intent(MainActivity.this, Activity2.class);
        PendingIntent stentIntent = PendingIntent.getActivity(this, 0,
                iintent, PendingIntent.FLAG_UPDATE_CURRENT);

        contentView.setOnClickPendingIntent(R.id.notify_set, stentIntent);
        // Set text on a TextView in the RemoteViews programmatically.
        final String time = DateFormat.getTimeInstance().format(new Date())
                .toString();
        final String text = getResources().getString(R.string.collapsed, time);
        contentView.setTextViewText(R.id.textView, text);

        /*
         * Workaround: Need to set the content view here directly on the
         * notification. NotificationCompatBuilder contains a bug that prevents
         * this from working on platform versions HoneyComb. See
         * https://code.google.com/p/android/issues/detail?id=30495
         */
        notification.contentView = contentView;

        // Add a big content view to the notification if supported.
        // Support for expanded notifications was added in API level 16.
        // (The normal contentView is shown when the notification is collapsed,
        // when expanded the
        // big content view set here is displayed.)
        // if (Build.VERSION.SDK_INT >= 16) {
        // // Inflate and set the layout for the expanded notification view
        // RemoteViews expandedView =
        // new RemoteViews(getPackageName(), R.layout.notification_expanded);
        // notification.bigContentView = expandedView;
        // }
        // END_INCLUDE(customLayout)

        // START_INCLUDE(notify)
        // Use the NotificationManager to show the notification
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(5270, notification);

        // END_INCLUDE(notify)
    }
}
