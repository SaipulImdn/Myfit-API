public class RegistrationActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        Button btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
            }
        });
    }
}

public class TrackingActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor stepSensor;

    private TextView tvSteps;
    private int stepCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        tvSteps = findViewById(R.id.tv_steps);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            stepCount = (int) event.values[0];
            tvSteps.setText(String.valueOf(stepCount));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}

public class FoodDiaryActivity extends AppCompatActivity {
    private EditText etFoodName;
    private Button btnAddFood;
    private ListView lvFoodList;
    private List<String> foodList;
    private ArrayAdapter<String> foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_diary);

        etFoodName = findViewById(R.id.et_food_name);
        btnAddFood = findViewById(R.id.btn_add_food);
        lvFoodList = findViewById(R.id.lv_food_list);

        foodList = new ArrayList<>();
        foodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foodList);
        lvFoodList.setAdapter(foodAdapter);

        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = etFoodName.getText().toString();
                foodList.add(foodName);
                foodAdapter.notifyDataSetChanged();
                etFoodName.setText("");
            }
        });
    }
}

public class ReminderActivity extends AppCompatActivity {
    private Button btnSetReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        btnSetReminder = findViewById(R.id.btn_set_reminder);
        btnSetReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        // Mengatur pengingat menggunakan JobScheduler
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        // Membuat komponen JobInfo untuk pengingat
        ComponentName componentName = new ComponentName(MainActivity.this, ReminderJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(1, componentName);

        // Mendapatkan waktu pengingat (misalnya 10 detik dari sekarang)
        long reminderTime = System.currentTimeMillis() + 10 * 1000; // 10 detik

        // Mengatur waktu trigger dan jenis pengulangan (jika diperlukan)
        builder.setMinimumLatency(reminderTime);

        // Menjadwalkan job dengan JobScheduler
        JobInfo jobInfo = builder.build();
        jobScheduler.schedule(jobInfo);

        Toast.makeText(MainActivity.this, "Pengingat telah diatur", Toast.LENGTH_SHORT).show();
    } else {
        // Mengatur pengingat menggunakan AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Mendapatkan waktu pengingat (misalnya 10 detik dari sekarang)
        long reminderTime = System.currentTimeMillis() + 10 * 1000; // 10 detik

        // Membuat intent untuk menjalankan BroadcastReceiver saat pengingat terpicu
        Intent intent = new Intent(MainActivity.this, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                MainActivity.this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Mengatur pengingat menggunakan AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);
        }

        Toast.makeText(MainActivity.this, "Pengingat telah diatur", Toast.LENGTH_SHORT).show();
    }
}

        });
    }
}
