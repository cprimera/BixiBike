//
//  AppDelegate.m
//  BixiBike
//
//  Created by Christopher Primerano on 13-02-01.
//  Copyright (c) 2013 Christopher Primerano. All rights reserved.
//

#import "AppDelegate.h"

@implementation AppDelegate

Station *currentStation;
NSString *currentElement;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    // Override point for customization after application launch.
    if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPad) {
        UISplitViewController *splitViewController = (UISplitViewController *)self.window.rootViewController;
        splitViewController.presentsWithGesture = NO;
        
        UINavigationController *navigationController = [splitViewController.viewControllers lastObject];
        splitViewController.delegate = (id)navigationController.topViewController;
        
        ((ListViewController *)((UINavigationController *)[splitViewController.viewControllers objectAtIndex:0]).topViewController).detailViewController = navigationController.topViewController;
    }
    return YES;
}
							
- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
    
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    
    NSXMLParser *parser = [[NSXMLParser alloc] initWithContentsOfURL:[NSURL URLWithString:@"https://toronto.bixi.com/data/bikeStations.xml"]];
    parser.delegate = self;
    _stations = [[NSMutableArray alloc] init];
    [parser parse];
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

#pragma mark -
#pragma mark Parser

-(void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
    if ([elementName isEqualToString:@"station"]) {
        currentStation = [[Station alloc] init];
    } else {
        if (currentElement == nil) {
            currentElement = @"";
        }
    }
}

-(void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string {
    currentElement = [currentElement stringByAppendingString:string];
}

-(void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {
    if ([elementName isEqualToString:@"station"]) {
        [_stations addObject:currentStation];
    } else if ([elementName isEqualToString:@"name"]) {
        [currentStation setName:currentElement];
        currentElement = nil;
    } else if ([elementName isEqualToString:@"lat"]) {
        CLLocationCoordinate2D location = CLLocationCoordinate2DMake([currentElement doubleValue], currentStation.coord.longitude);
        [currentStation setCoord:location];
        currentElement = nil;
    } else if ([elementName isEqualToString:@"long"]) {
        CLLocationCoordinate2D location = CLLocationCoordinate2DMake(currentStation.coord.latitude, [currentElement doubleValue]);
        [currentStation setCoord:location];
        currentElement = nil;
    } else if ([elementName isEqualToString:@"nbBikes"]) {
        [currentStation setNumBikes:[currentElement intValue]];
        currentElement = nil;
    } else if ([elementName isEqualToString:@"nbEmptyDocks"]) {
        [currentStation setNumSpots:[currentElement intValue]];
        currentElement = nil;
    } else {
        currentElement = nil;
    }
}

-(void)updateData:(UIRefreshControl *)refresh {
    NSXMLParser *parser = [[NSXMLParser alloc] initWithContentsOfURL:[NSURL URLWithString:@"https://toronto.bixi.com/data/bikeStations.xml"]];
    parser.delegate = self;
    _stations = [[NSMutableArray alloc] init];
    [parser parse];
    [refresh endRefreshing];
}

@end
