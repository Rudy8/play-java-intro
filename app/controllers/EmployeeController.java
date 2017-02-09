package controllers;

import models.Employees;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;

import static play.libs.Json.toJson;

public class EmployeeController extends Controller
{
    private final FormFactory formFactory;
    private final JPAApi jpaApi;

    @Inject
    public EmployeeController(FormFactory formFactory, JPAApi jpaApi)
    {
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
    }

    /*public Result index() {
        return ok(views.html.index.render());
    }*/


    @Transactional (readOnly = true)
    public Result editEmployee(Long employeeId)
    {
        Employees employee = (Employees)jpaApi.em()
                .createQuery("select e from Employees e where employeeId = :id")
                .setParameter("id", employeeId).getSingleResult();
        return ok(views.html.updateEmployee.render(employee));
    }

    @Transactional
    public Result updateEmployee()
    {
        DynamicForm postedForm = formFactory.form().bindFromRequest();
        Long employeeId = new Long(postedForm.get("employeeId"));
        String firstName = postedForm.get("firstName");
        String lastName = postedForm.get("lastName");
        String title = postedForm.get("title");


        Employees employee = (Employees) jpaApi.em()
                .createQuery("select e from Employees e where employeeId = :id")
                .setParameter("id", employeeId).getSingleResult();

        employee.firstName = firstName;
        employee.lastName = lastName;
        employee.title = title;



        jpaApi.em().persist(employee);

        return redirect(routes.EmployeeController.index());


    }




        @Transactional(readOnly = true)
    public Result getEmployees() {
        List<Employees> employee = (List<Employees>) jpaApi.em().createQuery("select e from Employees e").getResultList();
        return ok(toJson(employee));
    }

    @Transactional(readOnly = true)
    public Result index()
    {
        List<Employees> employees = (List<Employees>) jpaApi.em().
                createQuery("select e from Employees e").getResultList();

        return ok(views.html.employees.render(employees));
    }

    @Transactional (readOnly = true)
    public Result getPicture(Long id)
{
        Employees employee = (Employees) jpaApi.em().
        createQuery("select e from Employees e where employeeId = :id").setParameter("id",id).getSingleResult();

       if (employee.photo == null)
            {
        return null;
        }
        else
            {
                return ok(employee.photo).as("image/bmp");
            }

}

}


