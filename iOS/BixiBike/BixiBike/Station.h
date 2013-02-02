//
//  Station.h
//  BixiBike
//
//  Created by Christopher Primerano on 13-02-01.
//  Copyright (c) 2013 Christopher Primerano. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>

@interface Station : NSObject

@property (strong, nonatomic) NSString *name;
@property (nonatomic) CLLocationCoordinate2D coord;
@property (nonatomic) NSInteger numBikes;
@property (nonatomic) NSInteger numSpots;

@end
