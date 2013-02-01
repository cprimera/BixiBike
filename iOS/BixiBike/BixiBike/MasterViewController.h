//
//  MasterViewController.h
//  BixiBike
//
//  Created by Christopher Primerano on 13-02-01.
//  Copyright (c) 2013 Christopher Primerano. All rights reserved.
//

#import <UIKit/UIKit.h>

@class DetailViewController;

@interface MasterViewController : UITableViewController

@property (strong, nonatomic) DetailViewController *detailViewController;

@end
