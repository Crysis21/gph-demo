## Giphy Android App and Fresco

The Giphy app has reached it's 3rd major iteration. We focused on delivering new features, redesigning the home page to support stories, but also to improve our apps performance. We did some little tweaks, like converting the code to kotlin, refactoring the navigation stack and most importantly, how we render GIFs.

We head out in the wild to find a suitable replacement for our current image rendering technology and we soon had to decide between the two major image loading libraries, that offer support for GIF and WebP format:
1. Glide - probably the most popular choice for image loading in android, supported by Google
2. Fresco - created by Facebook for efficient and fast image loading.

https://giphy.com/gifs/apartmentguidesocial-confused-thinking-3diE2vbIarCXWXaN15

As both libraries offer support for disk caching, efficient image loading, post-processors, we had to take a decision based on animated drawable performance. 

We quickly drafted a demo app in which we loaded a ton of gifs, similar to our own application. We noticed that glide had problems rendering multiple sources at once, noticing dropped frames from the rendering compared to fresco. 


//Placeholder for gif comparison

Doing a quick search about Glide's performance with GIFs, we found multiple issues on GitHub related to this issue. Developers made it clear that performance can't be improved, unless they rewrite their rendering engine from scratch. At this point, it was clear we have a winner: Fresco

### Integrating Fresco
Setting up fresco was easy. Because we use GIF renditions with multiple qualities (e.q. thumbnails, gif details, story feed), we decided to setup 2 cache configs. In this way, fresco will not be forced to evict a lot of small thumbnails, because the cache will quickly fill with high quality images.

``` kotlin
val previewsDiskConfig = DiskCacheConfig.newBuilder(this)  
        .setMaxCacheSize(250L * ByteConstants.MB).build()  
val qualityDiskConfig = DiskCacheConfig.newBuilder(this)  
        .setMaxCacheSize(250L * ByteConstants.MB).build()  
val config = ImagePipelineConfig.newBuilder(this)  
        .setSmallImageDiskCacheConfig(previewsDiskConfig)  
        .setMainDiskCacheConfig(qualityDiskConfig)  
        .build()  
Fresco.initialize(this, config)
```
For rendering we used the `SimpleDraweeView` class. With just some small additions to support our SDK models out of the box, we had fresco rendering our GIFs in no time. Loading a image into a `DraweeView` basically means creating a new controller and assigning it to the view

```kotlin
val newController = Fresco.newDraweeControllerBuilder()  
        .setUri(uri)  
        .setOldController(draweeView.controller)  
        .setControllerListener(getControllerListener())  
        .build()
draweeView.controller = newController
```
Easy, fast, fun! Launch it!
https://media.giphy.com/media/Nweu3IeBIZIvm/giphy.gif

## Fresco limitations
When opening a GIF details page or a story, the app loads a set of image renditions, starting from low-quality images to high-quality ones. We soon discovered that when making a simple request to change the resource of a `DraweeView`, fresco will clear the current content, display the placeholder, then load the new resource. Even with the resources preloaded, there is a noticeable flickering.

To solve this issue we found 2 solutions:
1. Fresco provides a low-quality/high-quality schema for loading images. Unfortunately, in this schema, the low-quality image is not animated.
2. Use a `RetainingDataSource`. This allows us to keep the same `DraweeController` and replace it's content. Unfortunately, we couldn't make animated images play.

None of the above solutions worked out of the box, so we decided to fork fresco and fix the problem.  Analyzing fresco lifecycle, we identified the following actors responsible for loading and rendering images:

![enter image description here](https://github.com/Crysis21/gph-demo/blob/master/diagram.png)

While `RetainingDataSource` looked like what we were looking for,  it turns out that this data source, does it's magic by lying to the underlying fresco implementation, keeping it in a forever `PROGRESS` state. That means the `DraweeControllers` will never be notified when an GIF is loaded and they will never start to play.

**Fixing RetainingDataSource**
Solving the issue in our case meant:
1. Let  `DataSource` provide multiple results. In our loading flow, a new result ment a GIF image of a higher quality. 
2. Mark `RetainingDataSource` as a data source capable of delivering multiple results.
3. Modify the fresco `DraweeController` base class to deliver each result to it's drawee controlers.  At this point, controllers will start playing the animated drawables.

