//
//  Annotation.h
//  BixiBike
//
//  Created by Christopher Primerano on 13-02-01.
//  Copyright (c) 2013 Christopher Primerano. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>
#import <MapKit/MapKit.h>

@interface Annotation : NSObject <MKAnnotation>

@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;
@property (nonatomic, readonly, copy) NSString *title;
@property (nonatomic, readonly, copy) NSString *subtitle;
@property (nonatomic) NSInteger closest;

- (void)setCoordinate:(CLLocationCoordinate2D)newCoordinate;
- (void)setTitle:(NSString *)title;
- (void)setSubtitle:(NSString *)subtitle;

@end
