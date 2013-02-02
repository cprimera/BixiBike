//
//  Annotation.m
//  BixiBike
//
//  Created by Christopher Primerano on 13-02-01.
//  Copyright (c) 2013 Christopher Primerano. All rights reserved.
//

#import "Annotation.h"

@implementation Annotation

- (void)setCoordinate:(CLLocationCoordinate2D)newCoordinate {
    _coordinate = newCoordinate;
}

- (void)setTitle:(NSString *)title {
    _title = title;
}

- (void)setSubtitle:(NSString *)subtitle {
    _subtitle = subtitle;
}

@end
