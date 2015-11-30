# LeftDB-android
Is a simple SQLite ORM for Android. Fork of the [RightUtils](https://github.com/manfenixhome/RightUtils) database utils.

### Download
**Gradle dependency:**
``` groovy
repositories {
  jcenter()
}
    
dependencies {
  compile 'com.github.andreyrage:leftdb:1.0-beta1'
}
```

### Getting started
##### 1. Create helper:
``` java
public class DbHelper extends LeftDBUtils {
    private static volatile DbHelper sInstance;

    private DbHelper() {
        ...
    }

    public static DbHelper getInstance(Context context) {
        DbHelper localInstance = sInstance;
        if (localInstance == null) {
            synchronized (DbHelper.class) {
                localInstance = sInstance;
                if (localInstance == null) {
                    sInstance = localInstance = new DbHelper();
                    localInstance.setDBContext(context, "sample.sqlite", 1);
                }
            }
        }
        return localInstance;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        createTable(db, SimpleEntity.class);
    }
    
    ...
}
```

##### 2. Create entity with default constructor:
``` java
public class SimpleEntity {

    @ColumnAutoInc private Long id;
    @ColumnName("name") private String entityName;
    @ColumnDAO private Properties properties;

    public SimpleEntity() {
    }
    
    ...
}
```

##### 3. Now you can save and restore objects from database.
For example:
``` java
DbHelper dbHelper = DbHelper.getInstance(this);

SimpleEntity simpleEntity = new SimpleEntity();

dbHelper.add(simpleEntity); //save in the database

List<SimpleEntity> entities 
    = dbHelper.getAll(SimpleEntity.class); //get all SimpleEntity objects from database
  
dbHelper.delete(simpleEntity); //delete object from database

```

For more information, see [example](https://github.com/AndreyRage/LeftDB-android/tree/master/sample).
