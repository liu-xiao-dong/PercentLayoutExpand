# PercentLayoutExpand
官方百分比库扩展，支持布局本身支持宽高比及子控件支持宽高比设置，支持布局圆角、圆形及描边功能。

### Specs
  ![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)
  
***
直接上demo截图

![PercentLayoutExpand](https://github.com/liu-xiao-dong/PercentLayoutExpand/blob/master/screenshot/screenshot_full.png?raw=true) ![PercentLayoutExpand](https://github.com/liu-xiao-dong/PercentLayoutExpand/blob/master/screenshot/screenshot_aspect.png?raw=true)  
![PercentLayoutExpand](https://github.com/liu-xiao-dong/PercentLayoutExpand/blob/master/screenshot/screenshot_clip.png?raw=true)

### 下载体验：
![PercentLayoutExpand](https://github.com/liu-xiao-dong/PercentLayoutExpand/blob/master/screenshot/download.png?raw=true)
### 使用步骤：
1.添加gradle依赖：

```gradle

compile 'com.lxd.percent:percent:1.0.0'

```
2.在xml中使用PercentLinerLayout、PercentRelativeLayout、PercentFrameLayout;

 （1）宽高比属性使用：

```gradle
<com.lxd.percent.PercentLinearLayout
        android:orientation="horizontal"
        android:layout_width="match_parent"
        app:layout_selfAspectRatio="200%" //使布局本身支持宽高比
        android:layout_height="wrap_content">
        <TextView
            android:layout_width="0dp"
            android:layout_height="match_parent"
            app:layout_aspectRatio="100%" //子控件支持宽高比
           />
</com.lxd.percent.PercentLinearLayout>
```

  （2）百分比属性使用：
  
  ```gradle
<com.lxd.percent.PercentLinearLayout
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <TextView
            android:layout_width="match_parent"
            android:layout_height="0dp"
            app:layout_heightPercent="50%"  //父布局宽度的百分之50
            app:layout_marginPercent="10%"  //距左右margin为（父布局x） * 10%  距上下margin为 (父布局y) * 10%
            
            //其它right、top、bottom用法同下
            app:layout_marginLeftPercentX="10%"  //marginLeft=（父布局x） * 10%
            app:layout_marginLeftPercentY="10%"  //marginLeft=（父布局y） * 10%
            app:layout_marginLeftPercentScreenX="10%"  //marginLeft=（屏幕宽度 x） * 10%
            
            //设置字体大小
            app:layout_widthTextSizePercent="3%"  //字体大小占父布局宽度的百分比
            app:layout_heightTextSizePercent="50%"  //字体大小占父布局高度的百分比
            app:layout_screenWidthTextSizePercent="2%"  //字体大小占屏幕宽度的百分比
           />
</com.lxd.percent.PercentLinearLayout>
```

  （3）圆角、圆形及描边的使用：
  
  ```gradle
<com.lxd.percent.PercentFrameLayout
        app:enable_clip="true" //必须声明，否则其它属性不生效
        android:layout_margin="15dp"
        app:round_corner_top_left="20dp" //左上圆角半径
        app:round_corner_top_right="20dp" //右上圆角半径
        app:round_corner_bottom_right="20dp" //右下圆角半径
        app:round_corner="20dp" //四个角的圆角半径相同时使用
        app:layout_stroke_width="5dp" //描边宽度
        app:layout_stroke_color="@color/colorAccent" //描边颜色
        app:round_as_circle="true" //圆形，不与圆角同时使用
        android:layout_width="100dp"
        android:layout_height="100dp">
        <ImageView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="#0f0"
            />
    </com.lxd.percent.PercentFrameLayout>
```
 
 ### 注意事项：
 1.布局本身使用layout_selfAspectRatio属性的时候，当前未定义方向为warp_content,如定义为0dp则不生效；
 
 2.布局本身在x  or y方向上使用layout_selfAspectRatio属性时，子控件在该方向不可使用layout_widthPercent or layout_heightPercent属性，否则按布局的父布局的该放方向尺寸为基数计算百分比；
