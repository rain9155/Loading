# Loading
[![](https://jitpack.io/v/rain9155/Loading.svg)](https://jitpack.io/#rain9155/Loading)
###  一个简单的状态视图切换库，最多二行代码配置加载中、加载失败、空数据的视图，支持全局配置，欢迎大家star、fork，如有问题请[issue](<https://github.com/rain9155/Loading/issues>)。

## Pre

在我们做项目的时候，经常会遇到视图切换，比如一进入app,，就要从网络上加载一些数据，此时显示加载中视图，如果数据加载成功，就会切换为内容视图，如果数据加载失败，就会显示一个重新加载的视图或者加载失败的视图，如果没有数据，就会显示一个空数据视图。

而在做这些视图切换时，我们一般是在xml布局中先写好各种状态的视图，然后把visible设置为false，把想要显示的visible设置为true，然后如果在代码中进行视图切换时，就通过View的setVisible方法进行切换，这种方法也可以，但是不够优雅，而且会使得xml布局臃肿，一个xml布局中应该只有有关内容显示的视图，而不应该掺杂进其他诸如加载中、加载失败等视图。

## How to do？

所以我理想中的布局是内容、加载中、加载失败、空数据等视图应该分别写到一个xml文件中，等用的时候再把它显示出来，所以我定义了一个[StatusView](<https://github.com/rain9155/Loading/blob/master/loading/src/main/java/com/example/loading/StatusView.java>)，它是专门把放置内容、加载中、加载失败、空数据等视图，然后把它替换掉内容视图在原来布局的位置，这样就可以通过**StatusView**控制这些视图的切换，我还定义了一个[Loading](<https://github.com/rain9155/Loading/blob/master/loading/src/main/java/com/example/loading/Loading.java>)全局帮助类，用于帮助添加视图等操作，更多实现请查看代码。

该库提供的功能如下：

* 支持Activity（带状态栏和不带状态栏），Fragment（ViewPager情况和逐个Fragment情况）和View（单个View，多个View）。
* 暂时不支持RecyclerView、ListView和GildView这些有回收复用的View。
* 提供切换加载中、空数据页、错误页或重新加载、内容页功能

## Preview
![loading1](/screenshots/loading1.gif)
![loading2](/screenshots/loading2.gif)
## DownLoad
更多请下载查看:<br>
![qrcode](/screenshots/ORcode.png)
## How to install?

### step1:

添加jitpack库到项目根目录的build.gradle中：

```java
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

### step2：

在app目录的build.gradle中添加该库的引用：

```java
implementation 'com.github.rain9155:Loading:v1.0'
```

## How to use?

每一个页面都应该维护一个StatusView实例，通过Loading的Builder方法创建一个StatusView实例，然后调用StatusView里面的方法在需要的时候切换视图。

StatusView中相应方法说明如下：

| 方法          | s说明                       |
| :------------ | --------------------------- |
| showLoading() | 显示加载中视图              |
| showSuccess() | 显示内容视图                |
| showError()   | 显示错误视图 / 重新加载视图 |
| showEmpty()   | 显示空数据视图              |

Loading中相应方法说明如下：

| 方法                                         | 说明                                                         |
| :------------------------------------------- | :----------------------------------------------------------- |
| addLoadingView()                             | 添加加载中视图                                               |
| warp(Activity activity) / wrap(View view)    | 添加内容视图，如果是Activity就直接串Activity的实例，如果是Fragment就传Fragment的View布局，如果是View就直接传View |
| addErrorView()                               | 添加错误视图 / 重新加载视图                                  |
| addEmptyView()                               | 添加空数据视图                                               |
| withReload(final Runnable task, int retryId) | 如果添加的错误视图是重新加载视图，那么这里可以传一个触发重新加载逻辑的task进去，retryId就是你触发重新加载的控件Id |
| beginBuildStatusView() / create()            | 这两个方法要配对使用，表示创建一个StatusView                 |
| beginBuildCommit() / commit()                | 这两个方法要配对使用，在全局配置时使用，表示把状态视图暂时添加到Loading中 |

下面以Activity为例。

### 1、在Application里全局配置状态视图，在各个页面（Activity/Fragment/View）创建StatusView实例

大多数情况下我们app中所有的加载中，错误页等视图都是统一的，所以我们可以在Application中全局配置，用add方法：

```java
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Loading.beginBuildCommit()
                .addEmptyView(R.layout.empty_view)
                .addLoadingView(R.layout.loading_view)
                .addErrorView(R.layout.reload_view)
                .commit();
    }
}
```

记得在AndroidManifest.xml中添加**name**属性：

```java
<application
        android:name="com.example.loading2.app.App"
        .../>
```

然后在我们想要操作的Activity中把Activity的内容视图用wrap方法添加进StatusView：

```java
public class ActionBarActivity extends AppCompatActivity {
    
    StatusView mStatusView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar);
        
        mStatusView = Loading.beginBuildStatusView(this)
                .warp(this)
                .withReload(() -> {
                    mStatusView.showLoading();
                    mHandler.postDelayed(() -> mStatusView.showSuccess(), 3000);
                }, R.id.iv_reload)
                .create();
    }
}

```

然后在需要切换视图的地方就调用如下相应的方法即可：

* mStatusView.showLoading();
* mStatusView.showSuccess();
* mStatusView.showError();
* mStatusView.showEmpty();

### 2、单独为每个页面（Activity/Fragment/View）配置状态视图，并创建StatusView

你可以单独为每个页面用add方法创建状态视图，如下：

```java
public class ActionBarActivity extends AppCompatActivity {
    
    StatusView mStatusView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar);
        
        mStatusView = Loading.beginBuildStatusView(this)
                .warp(this)
                .withReload(() -> {
                    mStatusView.showLoading();
                    mHandler.postDelayed(() -> mStatusView.showSuccess(), 3000);
                }, R.id.iv_reload)
             	.addEmptyView(R.layout.empty_view)
                .addLoadingView(R.layout.loading_view)
                .addErrorView(R.layout.reload_view)
                .create();
    }
```

然后在需要切换视图的地方就调用mStatusView中相应的方法即可，跟1的一样。

这里需要说明的是，在单独页面add的状态视图可以覆盖在Application中add的状态视图，所以如果你有些状态视图是全局统一的，就在Application中配置，如果是单独为页面设计的就单独在页面里add。

更多使用请参考[Demo](<https://github.com/rain9155/Loading/tree/master/app/src/main/java/com/example/loading>).

## License

```
Copyright 2019 rain9155

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License a

          http://www.apache.org/licenses/LICENSE-2.0 
          
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
   
```
