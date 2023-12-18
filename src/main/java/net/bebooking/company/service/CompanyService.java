package net.bebooking.company.service;



import net.bebooking.company.dto.CompanyDetails;
import net.bebooking.company.dto.CompanyList;
import net.bebooking.company.dto.CreateCompanyRequest;
import net.bebooking.company.dto.UpdateDetailsCompanyRequest;
import net.bebooking.company.model.CompanyId;
import net.bebooking.principal.model.Principal;

public interface CompanyService {
    CompanyId createCompany(Principal principal, CreateCompanyRequest req); //ID

    void updateDetails(Principal principal, UpdateDetailsCompanyRequest req);
    CompanyDetails getById(Principal principal, CompanyId id);

    CompanyList getAll(Principal principal);
}
