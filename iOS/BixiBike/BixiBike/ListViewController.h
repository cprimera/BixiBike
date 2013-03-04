//
//  ListViewController.h
//  BixiBike
//
//  Created by Christopher Primerano on 13-02-02.
//  Copyright (c) 2013 Christopher Primerano. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"
#import "MapViewController.h"

@interface ListViewController : UITableViewController <UITableViewDataSource, UITableViewDelegate, CLLocationManagerDelegate, UISplitViewControllerDelegate, MKMapViewDelegate>

@property (strong, nonatomic) IBOutlet UITableView *tableView;
@property (strong, nonatomic) UIViewController *detailViewController;

-(IBAction)map:(id)sender;

@end
