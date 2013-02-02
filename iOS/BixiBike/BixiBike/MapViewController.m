//
//  MapViewController.m
//  BixiBike
//
//  Created by Christopher Primerano on 13-02-01.
//  Copyright (c) 2013 Christopher Primerano. All rights reserved.
//

#import "MapViewController.h"

#define DEGREES_TO_RADIANS (M_PI/180.0)
#define WGS84_A	(6378137.0)
#define WGS84_E (8.1819190842622e-2)


@implementation MapViewController

AppDelegate *appDelegate;
bool updateRegion = FALSE;
MKUserLocation *user;

-(void)viewDidLoad {
    self.navigationItem.title = @"Map";
}

-(void)viewWillAppear:(BOOL)animated {
    if (_highlight == nil) {
        updateRegion = TRUE;
    }
}

-(void)viewDidAppear:(BOOL)animated {
    if (_highlight != nil) {
        [_map setRegion:MKCoordinateRegionMake(_highlight.coord, MKCoordinateSpanMake(0.01f, 0.01f)) animated:NO];
    }
}

-(void)mapView:(MKMapView *)mapView didUpdateUserLocation:(MKUserLocation *)userLocation {
    user = userLocation;
    if (updateRegion) {
        [_map setRegion:MKCoordinateRegionMake(userLocation.coordinate, MKCoordinateSpanMake(0.01f, 0.01f)) animated:NO];
        updateRegion = FALSE;
    }
    [self updateData];
}

-(void)mapViewDidFinishLoadingMap:(MKMapView *)mapView {
    [self updateData];
}

-(MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id<MKAnnotation>)annotation {
    if ([annotation class] == [MKUserLocation class]) {
        return nil;
    }
    if (_highlight == nil) {
        if (((Annotation *)annotation).closest == 1) {
            MKPinAnnotationView *pin = (MKPinAnnotationView *)[mapView dequeueReusableAnnotationViewWithIdentifier:@"pinG"];
            if (pin == nil) {
                pin = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"pinG"];
            }
            [pin setAnnotation:annotation];
            [pin setPinColor:MKPinAnnotationColorGreen];
            [pin setCanShowCallout:YES];
            //[pin setAnimatesDrop:YES];
            return pin;
        } else {
            MKPinAnnotationView *pin = (MKPinAnnotationView *)[mapView dequeueReusableAnnotationViewWithIdentifier:@"pin"];
            if (pin == nil) {
                pin = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"pin"];
            }
            [pin setAnnotation:annotation];
            [pin setPinColor:MKPinAnnotationColorRed];
            [pin setCanShowCallout:YES];
            //[pin setAnimatesDrop:YES];
            return pin;
        }
    } else {
        if ([((Annotation *)annotation).title isEqualToString:_highlight.name]) {
            MKPinAnnotationView *pin = (MKPinAnnotationView *)[mapView dequeueReusableAnnotationViewWithIdentifier:@"pinP"];
            if (pin == nil) {
                pin = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"pinP"];
            }
            [pin setAnnotation:annotation];
            [pin setPinColor:MKPinAnnotationColorPurple];
            [pin setCanShowCallout:YES];
            //[pin setAnimatesDrop:YES];
            return pin;
        } else {
            MKPinAnnotationView *pin = (MKPinAnnotationView *)[mapView dequeueReusableAnnotationViewWithIdentifier:@"pin"];
            if (pin == nil) {
                pin = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"pin"];
            }
            [pin setAnnotation:annotation];
            [pin setPinColor:MKPinAnnotationColorRed];
            [pin setCanShowCallout:YES];
            //[pin setAnimatesDrop:YES];
            return pin;
        }
    }
}

#pragma mark -
#pragma mark SplitViewController

-(void)splitViewController:(UISplitViewController *)svc willHideViewController:(UIViewController *)aViewController withBarButtonItem:(UIBarButtonItem *)barButtonItem forPopoverController:(UIPopoverController *)pc {
    barButtonItem.title = @"Master";
    [self.navigationItem setLeftBarButtonItem:barButtonItem animated:YES];
}

-(void)splitViewController:(UISplitViewController *)svc willShowViewController:(UIViewController *)aViewController invalidatingBarButtonItem:(UIBarButtonItem *)barButtonItem {
    [self.navigationItem setLeftBarButtonItem:nil animated:YES];
}

#pragma mark -
#pragma mark Setters

-(void)setHighlight:(Station *)highlight {
    _highlight = highlight;

    for (Annotation *a in [_map annotations]) {
        if ([a class] != [MKUserLocation class]) {
            [_map removeAnnotation:a];
            [_map addAnnotation:a];
        }
    }
    [_map setRegion:MKCoordinateRegionMake(_highlight.coord, MKCoordinateSpanMake(0.01f, 0.01f)) animated:NO];
}

-(void)updateData {
    appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
        
    NSInteger close = 0;
    double dist = DBL_MAX;
    NSMutableArray *annotations = [[NSMutableArray alloc] init];
    for (Station *s in appDelegate.stations) {
        Annotation *an = [[Annotation alloc] init];
        [an setCoordinate:s.coord];
        [an setTitle:s.name];
        [an setSubtitle:[NSString stringWithFormat:@"Bikes Available: %d  Docks Available: %d", s.numBikes, s.numSpots]];
        [an setClosest:0];
        
        [annotations addObject:an];
        
        CLLocation *loc = [[CLLocation alloc] initWithLatitude:s.coord.latitude longitude:s.coord.longitude];
        
        double tempdist = [loc distanceFromLocation:[[CLLocation alloc] initWithLatitude:user.coordinate.latitude longitude:user.coordinate.longitude]];
        if (dist >= tempdist) {
            dist = tempdist;
            close = [annotations indexOfObject:an];
        }
        
    }
    ((Annotation *)[annotations objectAtIndex:close]).closest = 1;
    for (Annotation *a in _map.annotations) {
        if ([a class] == [Annotation class]) {
            [_map removeAnnotation:a];
        }
    }
    [_map addAnnotations:annotations];
}

@end
