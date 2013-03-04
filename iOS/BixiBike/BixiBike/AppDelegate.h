//
//  AppDelegate.h
//  BixiBike
//
//  Created by Christopher Primerano on 13-02-01.
//  Copyright (c) 2013 Christopher Primerano. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import "Station.h"
#import "Stations.h"
#import "ListViewController.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate, NSXMLParserDelegate, UISplitViewControllerDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) Stations *stations;

-(void)updateData:(UIRefreshControl *)refresh;

@end
