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

-(void)refreshControlRequest {
    [appdelegate updateData:self.refreshControl];
    [self.tableView reloadData];
    [((MapViewController *)_detailViewController) updateData];
    //[self.refreshControl endRefreshing];
}

-(void)mapView:(MKMapView *)mapView didUpdateUserLocation:(MKUserLocation *)userLocation {
    location = userLocation;
    [self.tableView reloadData];
}

- (void)viewDidAppear:(BOOL)animated {
    appdelegate = ((AppDelegate *)[[UIApplication sharedApplication] delegate]);
    [appdelegate.stations sortUsingComparator:^NSComparisonResult(id obj1, id obj2) {
        Station *station1 = (Station *)obj1;
        Station *station2 = (Station *)obj2;
        CLLocation *stat1 = [[CLLocation alloc] initWithLatitude:station1.coord.latitude longitude:station1.coord.longitude];
        CLLocation *stat2 = [[CLLocation alloc] initWithLatitude:station2.coord.latitude longitude:station2.coord.longitude];
        CLLocation *me = [[CLLocation alloc] initWithLatitude:location.location.coordinate.latitude longitude:location.location.coordinate.longitude];
        if ([me distanceFromLocation:stat1] > [me distanceFromLocation:stat2]) {
            return (NSComparisonResult)NSOrderedDescending;
        }
        if ([me distanceFromLocation:stat1] < [me distanceFromLocation:stat1]) {
            return (NSComparisonResult)NSOrderedAscending;
        }
        return (NSComparisonResult)NSOrderedSame;
    }];
    [self.tableView reloadData];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
#warning Potentially incomplete method implementation.
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
#warning Incomplete method implementation.
    // Return the number of rows in the section.
    return appdelegate.stations.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    
    // Configure the cell...
    cell.textLabel.text = ((Station *)[appdelegate.stations objectAtIndex:indexPath.row]).name;
    cell.detailTextLabel.text = [NSString stringWithFormat:@"Bikes Available: %d  Docks Available: %d", ((Station *)[appdelegate.stations objectAtIndex:indexPath.row]).numBikes, ((Station *)[appdelegate.stations objectAtIndex:indexPath.row]).numSpots];
    
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
        [vc setHighlight:[appdelegate.stations objectAtIndex:indexPath.row]];
        [self.navigationController pushViewController:vc animated:YES];
    } else {
        ((MapViewController *)_detailViewController).highlight = [appdelegate.stations objectAtIndex:indexPath.row];
        [tableView deselectRowAtIndexPath:indexPath animated:YES];
    }
}

-(void)map:(id)sender {
    MapViewController *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"map"];
    [vc setHighlight:nil];
    [self.navigationController pushViewController:vc animated:YES];
}

@end
