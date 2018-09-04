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

