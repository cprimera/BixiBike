//
//  Stations.h
//  BixiBike
//
//  Created by Christopher Primerano on 13-02-02.
//  Copyright (c) 2013 Christopher Primerano. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "StationsProtocol.h"
#import "Station.h"

@interface Stations : NSObject <NSXMLParserDelegate>

@property (strong, nonatomic) NSMutableArray *stations;
@property (nonatomic) BOOL updating;
@property (nonatomic) CLLocationCoordinate2D coordinate;

-(void)requestUpdate;
//-(void)receiveNotifications:(id<StationsProtocol>)object;
//-(void)stopReceivingNotifications:(id<StationsProtocol>)object;
-(void)sort;

@end
