package de.geoinfoffm.registry.client.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class DatatableParameters
{
	public String sEcho;
	public String iDisplayStart;
	public String iDisplayLength;
	public String iColumns;
	public String iSortingsCols;
	public String sSearch;
	public Map<Integer, String> columns;
	public Map<String, String> sortingColumns;

	public DatatableParameters(Map<String, String> parameters) {
		sEcho = parameters.get("sEcho");
		iDisplayStart = parameters.get("iDisplayStart");
		iDisplayLength = parameters.get("iDisplayLength");
		iColumns = parameters.get("iColumns");
		iSortingsCols = parameters.get("iSortingCols");
		sSearch = parameters.get("sSearch");

		columns = new HashMap<Integer, String>();
		if (iColumns != null) {
			int columnCount = Integer.parseInt(iColumns);
			for (int i = 0; i < columnCount; i++) {
				columns.put(i, parameters.get("mDataProp_" + Integer.toString(i)));
			}
		}

		sortingColumns = new HashMap<String, String>();
		if (iSortingsCols != null) {
			int sortingCols = Integer.parseInt(iSortingsCols);
			for (int i = 0; i < sortingCols; i++) {
				String sortingCol = parameters.get("iSortCol_" + Integer.toString(i));
				String direction = parameters.get("sSortDir_" + Integer.toString(i));
				sortingColumns.put(columns.get(Integer.parseInt(sortingCol)), direction);
			}
		}
	}
	
	public Pageable createPageable() {
		Pageable pageable;
		if (this.iDisplayStart != null && this.iDisplayLength != null) {
			int startAt = Integer.parseInt(this.iDisplayStart);
			int length = Integer.parseInt(this.iDisplayLength);
			int pageNo = startAt / length;

			Sort sort;
			if (!this.sortingColumns.isEmpty()) {
				List<Order> orders = new ArrayList<Order>();
				for (String property : this.sortingColumns.keySet()) {
					String sortCol = property;
					if (property.equals("proposalStatus")) {
						sortCol = "status";
					}
					Order order = new Order(Direction.fromString(this.sortingColumns.get(property).toString().toUpperCase()), sortCol);
					orders.add(order);
				}
				sort = new Sort(orders);
			}
			else {
				// sort = new Sort(new Order("status"));
				sort = null;
			}

			pageable = new PageRequest(pageNo, length, sort);
		}
		else {
			pageable = new PageRequest(0, 10);
		}
		return pageable;
	}
}
