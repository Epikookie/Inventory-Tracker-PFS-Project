# Inventory-Tracker-PFS-Project
A	report	and	an	application	for	inventory	tracing.	i.e.	working	
out	where	inventory	is,	how	much	of	it	you	have,	who	supplies	it	etc.

# Kade 14/06/2023
Application TODO:
- Password verification and authentication, utilising bcrypt and salt - DONE
- Multi-search - DONE
- JUnit testing for security functions
- Additional Security Features:
>> Need timout after X incorrect login attempts

Wishlist:
- Manager console with CRUD operations for Item, Staff, Store and Supplier

# Kade 26-27/05/2023
# settings.json - switch for setting output path of Java .class files
"java.project.outputPath": ""

My files are currently compiling on my local disc, so not worried about it for now.

I found this in the OLD project (SAD) settings.json file:
{
    "java.project.sourcePaths": ["src"],
    "java.project.outputPath": "bin",
    "java.project.referencedLibraries": [
        "lib/**/*.jar"
    ]
}

So I think if we don't set the outputPath, it will default to the local machine, which is ideal (EG not in the Github Repo)
Alternative would be to set them all to 'bin', but then ignore that folder... but let's only do that if we need to

# Kade 27/05/2023
Database and all tables creating no problem. Testing addSupplier function and working.