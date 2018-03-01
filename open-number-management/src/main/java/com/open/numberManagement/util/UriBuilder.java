package com.open.numberManagement.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class UriBuilder {

    public URI requestUriWithId(long id) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(id).toUri();
    }

    public String getHrefWithId(String path, long id) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();
        
        return uri.getScheme() + "://" + uri.getAuthority() + "/" + path + id;
    }

}
