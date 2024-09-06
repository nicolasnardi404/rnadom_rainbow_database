package com.randomrainbow.springboot.demosecurity.dto;

import com.randomrainbow.springboot.demosecurity.entity.VideoStatus;

public record VideoWithEndpointDTO (int videoId, String title, String videoDescription, String videoLink, VideoStatus videoStatus, String token, String username){
}