li2latex - LinkedIn Profile to LaTeX-based Resume
=================================================

This is a commandline LinkedIn App that pulls your LinkedIn profile data through OAuth and generates a LaTex file for your resume.  LaTeX tools such as `pdflatex` is supposed to be pre-installed before using this app.

How to use it
-------------
The project is built with Scala (2.9.1) and SBT (0.11.2). So run the app simply by typing `sbt` and then in SBT console type `run`.  Press enter a couple of times if you would like to just keep the default settings.

How to extend it
----------------
Due to the limitation of LinkedIn API and the heterogeneous nature of people's profile/resume.  This app is not supposed to work out of box for everyone.  So please fork it and tune it against your own LinkedIn profile.

Below are serveral aspects that you probably would like to modify by yourself:

* **The LaTeX template**: A sample copy of my resume can be found at [http://dl.dropbox.com/u/6824415/li2latex/resume.pdf](http://dl.dropbox.com/u/6824415/li2latex/resume.pdf).  You may well wish to use other LaTeX resume templates. To do this, write your own copy of *TemplateProvider* which extends the `li2latex.template.TemplateProvider` trait. And then in `Laucher`, make the `Resume` instance use your own version of *TemplateProvider*.

* **The sections of your resume**: This is currently defined in `Launcher` using the following DSL:
	
		"contact"    >> "Contact Info" >> "phone-numbers,im-accounts,main-address",
		"positions"  >> "Work Experience",
		"educations" >> "Education",
		"skills"     >> "Skills",
		"projects"   >> "Project Experience"
On each line the first item is the field name in LinkedIn API. The second item is the section title that you would like it to appear in your resume.  The optional third item defines which specific fields to request for in the special case of **Contact Info** section. Note that there are fields for which the parser has not yet been implemented yet.  You may need to extend the `li2latex.model.FieldsParser` trait for your own purpose.

* **Information you need to fix-up**: Due to the limitation of LinkedIn API, there are some important fields that are missing, such as the start date and end date of your projects.  So this app provides some sort of fix-up workaround for this purpose.  By default you may define information you would like to fix up in `myfixup.xml` and update the `li2latex.util.LocalFixUp` object to make sure the types of fix-up you need do work.  You might also need to modify corresponding `FieldsParser` classes.