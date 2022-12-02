<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"  %>
<!--  <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<title>Insert title here</title>
</head>
<body>
<h1>Register Account</h1><br>
<form action="/register" method="post">
 <table>
<tr>
<td>Name</td>
</tr>
<tr>
<td><input name="name" type="text"/> </td>
</tr>
<td>Address</td>
</tr>
<tr>
<td><input name="address" type="text"/> </td>
</tr>
<td>Email</td>
</tr>
<tr>
<td><input name="email" type="text"/> </td>
</tr>
<td>PhoneNumber</td>
</tr>
<tr>
<td><input name="phone_No" type="text"/> </td>
</tr>
<td>UserName</td>
</tr>
<tr>
<td><input name="username" type="text"/> </td>
</tr>
<td>Password</td>
</tr>
<tr>
<td><input name="password" type="text"/> </td>
</tr>
<td>ConfirmPassword</td>
</tr>
<tr>
<td><input name="confirmpassword" type="text"/> </td>
</tr>



</table> 
</br>

<input name="submit" value="Register" type="submit"/> 


</form> 

</body>
</html>-->

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>
 <div class="container">
  <h1> Registration Form:</h1>
  <div class="card">
   <div class="card-body">
    <form action="/register" method="post">

     <div class="form-group row">
      <label for="name" class="col-sm-2 col-form-label">
       Name</label>
      <div class="col-sm-7">
       <input type="text" class="form-control" name="name"
        placeholder="Enter name">
      </div>
     </div>

     
     

     <div class=" form-group row">
      <label for="email" class="col-sm-2 col-form-label">
       Email</label>
      <div class="col-sm-7">
       <input type="text" class="form-control" name="email"
        placeholder="Enter Email">
      </div>
     </div>
     
      <div class=" form-group row">
      <label for="username" class="col-sm-2 col-form-label">
       Username</label>
      <div class="col-sm-7">
       <input type="text" class="form-control" name="username"
        placeholder="Enter Username">
      </div>
     </div>
     

     <div class="form-group row">
      <label for="password" class="col-sm-2 col-form-label">Password</label>
      <div class="col-sm-7">
       <input type="password" class="form-control" name="pass"
        placeholder="Enter Password">
      </div>
     </div>
     
     <div class="form-group row">
      <label for="cnfmpass" class="col-sm-2 col-form-label">Confirm Password</label>
      <div class="col-sm-7">
       <input type="password" class="form-control" name="cnfmpass"
        placeholder="Enter Password">
      </div>
     </div>

     <div class="form-group row">
      <label for="address" class="col-sm-2 col-form-label">Address</label>
      <div class="col-sm-7">
       <input type="text" class="form-control" name="address"
        placeholder="Enter Address">
      </div>
     </div>

     <div class="form-group row">
      <label for="contact" class="col-sm-2 col-form-label">Contact
       No</label>
      <div class="col-sm-7">
       <input type="text" class="form-control" name="contact"
        placeholder="Enter Contact Address">
      </div>
     </div>

     <button type="submit" class="btn btn-primary">Submit and Back to Login</button>
    
    </form>
    <br/>
      <form action="/index" method="get">
     <button type="submit" class="btn btn-primary">Cancel</button>
     </form>
   </div>
  </div>
 </div>
</body>
</html>
</html>