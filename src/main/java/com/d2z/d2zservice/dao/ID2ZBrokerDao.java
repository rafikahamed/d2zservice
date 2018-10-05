package com.d2z.d2zservice.dao;

import java.util.List;
import com.d2z.d2zservice.entity.PostcodeZone;

public interface ID2ZBrokerDao {
	
	public List<PostcodeZone> fetchAllPostCodeZone();

}
