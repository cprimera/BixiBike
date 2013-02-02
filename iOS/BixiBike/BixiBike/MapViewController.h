//
//  MapViewController.h
//  BixiBike
//
//  Created by Christopher Primerano on 13-02-01.
//  Copyright (c) 2013 Christopher Primerano. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "AppDelegate.h"
#import "Annotation.h"
#import <limits.h>

@interface MapViewController : UIViewController <MKMapViewDelegate, CLLocationManagerDelegate, UISplitViewControllerDelegate>

@property (strong, nonatomic) IBOutlet MKMapView *map;
@property (strong, nonatomic) Station *highlight;

-(void)updateData;

@end
