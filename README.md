# LeftDB-android
Is a simple SQLite ORM for Android. Fork of the [RightUtils](https://github.com/manfenixhome/RightUtils) database utils.

### Download
**Gradle dependency:**
``` groovy
repositories {
  jcenter()
}
    
dependencies {
  compile 'com.github.andreyrage:leftdb:1.6-beta1'
}
```

### How it work
To work with the database you just need to use a helper. LeftDB can save and restore objects from database.
See [Getting started](https://github.com/AndreyRage/LeftDB-android/wiki/Getting-started) for details.
``` java
DbHelper dbHelper = DbHelper.getInstance(this);

SimpleEntity simpleEntity = new SimpleEntity();

dbHelper.add(simpleEntity); //save in the database

List<SimpleEntity> entities 
    = dbHelper.getAll(SimpleEntity.class); //get all SimpleEntity objects from database
  
dbHelper.delete(simpleEntity); //delete object from database

```

To execute in not UI thread you can use AsyncCall:
``` java
AsyncCall.make(new AsyncCall.Call<List<SimpleEntity>>() {
    @Override
    public List<SimpleEntity> call() {
        return dbHelper.getAll(SimpleEntity.class)
    }
}, new AsyncCall.Do<List<SimpleEntity>>() {
    @Override
    public void doNext(List<SimpleEntity> simpleEntities) {
        // returned result to UI thread
    }
}).call();
```

For more information, see [example](https://github.com/AndreyRage/LeftDB-android/tree/master/sample) or [documentation](https://github.com/AndreyRage/LeftDB-android/wiki/Documentation).
