package com.store.security.store_security.service;

import com.store.security.store_security.dto.TrackDto;

public interface ITrackService {

	public TrackDto getTrackByOrder(Long idOrder);

	public TrackDto setTrack(Long idOrder, TrackDto trackDto);
}
