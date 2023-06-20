public class ReminderActivity extends AppCompatActivity {
    private Button btnSetReminder;
    private JobScheduler jobScheduler;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        btnSetReminder = findViewById(R.id.btn_set_reminder);
        btnSetReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReminder();
            }
        });

        jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    private void setReminder() {
        long reminderTime = System.currentTimeMillis() + 10 * 1000; // 10 detik

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Mengatur pengingat menggunakan JobScheduler
            ComponentName componentName = new ComponentName(ReminderActivity.this, ReminderJobService.class);
            JobInfo.Builder builder = new JobInfo.Builder(1, componentName);

            // Mengatur waktu trigger dan jenis pengulangan (jika diperlukan)
            builder.setMinimumLatency(reminderTime - System.currentTimeMillis());

            // Menjadwalkan job dengan JobScheduler
            JobInfo jobInfo = builder.build();
            jobScheduler.schedule(jobInfo);

            Toast.makeText(ReminderActivity.this, "Pengingat telah diatur", Toast.LENGTH_SHORT).show();
        } else {
            // Mengatur pengingat menggunakan AlarmManager
            Intent intent = new Intent(ReminderActivity.this, ReminderReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    ReminderActivity.this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);
            }

            Toast.makeText(ReminderActivity.this, "Pengingat telah diatur", Toast.LENGTH_SHORT).show();
        }
    }
}
