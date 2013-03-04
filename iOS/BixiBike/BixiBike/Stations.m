//
//  Stations.m
//  BixiBike
//
//  Created by Christopher Primerano on 13-02-02.
//  Copyright (c) 2013 Christopher Primerano. All rights reserved.
//

#import "Stations.h"

@interface Stations()

@end

@implementation Stations

//NSMutableArray *receivers;
NSString *currentElement;
Station *currentStation;
NSMutableArray *tempStations;

-(id)init{
    if (self = [super init]) {
        [self requestUpdate];
//        receivers = [[NSMutableArray alloc] init];
    }
    return self;
}

-(void)requestUpdate {
    _updating = YES;
    dispatch_queue_t q = dispatch_queue_create("station update queue", NULL);
    dispatch_async(q, ^{
        NSXMLParser *parser = [[NSXMLParser alloc] initWithContentsOfURL:[NSURL URLWithString:@"https://toronto.bixi.com/data/bikeStations.xml"]];
        parser.delegate = self;
        tempStations = [[NSMutableArray alloc] init];
        [parser performSelectorInBackground:@selector(parse) withObject:Nil];
    });
    //[self doUpdate];
}

//-(void)receiveNotifications:(__autoreleasing id<StationsProtocol>)object {
//    [receivers addObject:object];
//}
//
//-(void)stopReceivingNotifications:(id<StationsProtocol>)object {
//    [receivers removeObject:object];
//}

-(void)sort {
    //NSLog(@"%f, %f", self.coordinate.latitude, self.coordinate.longitude);
    if (CLLocationCoordinate2DIsValid(self.coordinate)) {
        tempStations = [NSMutableArray arrayWithArray:_stations];
        [tempStations sortUsingComparator:^NSComparisonResult(id obj1, id obj2) {
            Station *station1 = (Station *)obj1;
            Station *station2 = (Station *)obj2;
            CLLocation *stat1 = [[CLLocation alloc] initWithLatitude:station1.coord.latitude longitude:station1.coord.longitude];
            CLLocation *stat2 = [[CLLocation alloc] initWithLatitude:station2.coord.latitude longitude:station2.coord.longitude];
            CLLocation *me = [[CLLocation alloc] initWithLatitude:self.coordinate.latitude longitude:self.coordinate.longitude];
            if ([me distanceFromLocation:stat1] > [me distanceFromLocation:stat2]) {
                return (NSComparisonResult)NSOrderedDescending;
            }
            if ([me distanceFromLocation:stat1] < [me distanceFromLocation:stat1]) {
                return (NSComparisonResult)NSOrderedAscending;
            }
            return (NSComparisonResult)NSOrderedSame;
        }];
        _stations = tempStations;
    }
}

-(void)doUpdate {
    NSXMLParser *parser = [[NSXMLParser alloc] initWithContentsOfURL:[NSURL URLWithString:@"https://toronto.bixi.com/data/bikeStations.xml"]];
    parser.delegate = self;
    tempStations = [[NSMutableArray alloc] init];
    [parser performSelectorInBackground:@selector(parse) withObject:Nil];
}

-(void)parserDidEndDocument:(NSXMLParser *)parser {
    _stations = tempStations;
    _updating = NO;
    if (CLLocationCoordinate2DIsValid(self.coordinate)) {
        [self sort];
    }
    NSNotification *notification = [NSNotification notificationWithName:@"StationsUpdatedNotification" object:self];
    [[NSNotificationCenter defaultCenter] postNotification:notification];
//    for (id<StationsProtocol> object in receivers) {
//        dispatch_async(dispatch_get_main_queue(), ^{
//            [object dataUpdated];
//        });
//    }
}

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
        [tempStations addObject:currentStation];
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

#pragma mark - Property Functions

- (void)setCoordinate:(CLLocationCoordinate2D)coordinate {
    if (CLLocationCoordinate2DIsValid(coordinate)) {
        _coordinate = coordinate;
        [self sort];
    }
}

@end
