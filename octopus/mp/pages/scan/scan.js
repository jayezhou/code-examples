const app = getApp()

Page({
  data: { 
    markers: [{ 
      iconPath: '../../static/image/map_marker.png', 
      id: 0, 
      latitude: 39.970830, 
      longitude: 116.410440, 
      width: 30, 
      height: 30 
  }] 
  
  }, 
  openMap: function () { 
    wx.openLocation({ 
      latitude: 39.970830, 
      longitude: 116.410440, 
      scale: 14, 
      name: '商务大厦' 
  }) 
  }, 

})
