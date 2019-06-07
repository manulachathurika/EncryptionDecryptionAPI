package com.manula.encrypt.DAO;

import java.util.List;

import com.manula.encrypt.entity.Region;
import com.manula.encrypt.entity.Request;
import com.manula.encrypt.entity.Team;

public interface WebscrapeDAO {

	public List<Region> getAllRegions();

	public List<Team> getTeamByRegionId(Integer id);

	public void addRequest(Request request);

	public Request getRequestByRequestId(Integer requestId);

	public List<Request> getAllRequests();

	public void deleteRequest(Integer requestId);

	public void updateRequestByRequestId(Request request);

}
