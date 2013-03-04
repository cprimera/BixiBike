//
//  ListViewController.m
//  BixiBike
//
//  Created by Christopher Primerano on 13-02-02.
//  Copyright (c) 2013 Christopher Primerano. All rights reserved.
//

#import "ListViewController.h"

@interface ListViewController ()

@end

@implementation ListViewController

AppDelegate *appdelegate;
MKUserLocation *location;

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    appdelegate = ((AppDelegate *)[[UIApplication sharedApplication] delegate]);
    
    self.navigationItem.title = @"List";
    MKMapView *map = [[MKMapView alloc] init];
    map.delegate = self;
    map.showsUserLocation = YES;
    
    UIRefreshControl *refreshControl = [[UIRefreshControl alloc] init];
    [refreshControl addTarget:self action:@selector(refreshControlRequest) forControlEvents:UIControlEventValueChanged];
    refreshControl.attributedTitle = [[NSAttributedString alloc] initWithString:@"Updating"];
    [self setRefreshControl:refreshControl];
    
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
 
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

-(void)dataUpdated{
//    NSLog(@"Data Updated");
    [self.tableView reloadData];
    if ([self.refreshControl isRefreshing]) {
        [self.refreshControl endRefreshing];
    }
}

-(void)refreshControlRequest {
    if (appdelegate.stations.updating == NO) {
        [appdelegate.stations requestUpdate];
    }

}

-(void)mapView:(MKMapView *)mapView didUpdateUserLocation:(MKUserLocation *)userLocation {
    location = userLocation;
    [appdelegate.stations setCoordinate:location.location.coordinate];
}

- (void)viewDidAppear:(BOOL)animated {
//    [appdelegate.stations receiveNotifications:self];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(dataUpdated) name:@"StationsUpdatedNotification" object:appdelegate.stations];
    [appdelegate.stations sort];
    [self.tableView reloadData];
}

- (void)viewWillDisappear:(BOOL)animated {
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"StationsUpdatedNotification" object:appdelegate.stations];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    return appdelegate.stations.stations.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    
    // Configure the cell...
    cell.textLabel.text = ((Station *)[appdelegate.stations.stations objectAtIndex:indexPath.row]).name;
    cell.detailTextLabel.text = [NSString stringWithFormat:@"Bikes Available: %d  Docks Available: %d", ((Station *)[appdelegate.stations.stations objectAtIndex:indexPath.row]).numBikes, ((Station *)[appdelegate.stations.stations objectAtIndex:indexPath.row]).numSpots];
    
    return cell;
}

/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }   
    else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
        MapViewController *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"map"];
        [vc setHighlight:[appdelegate.stations.stations objectAtIndex:indexPath.row]];
        [self.navigationController pushViewController:vc animated:YES];
    } else {
        ((MapViewController *)_detailViewController).highlight = [appdelegate.stations.stations objectAtIndex:indexPath.row];
        [tableView deselectRowAtIndexPath:indexPath animated:YES];
    }
}

-(void)map:(id)sender {
    MapViewController *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"map"];
    [vc setHighlight:nil];
    [self.navigationController pushViewController:vc animated:YES];
}

@end
