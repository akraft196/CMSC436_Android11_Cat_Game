# Welcome to cmsc436 Fall 2020 semester  

### What is Git and how to install it  

Git is the industry standard for souce code management in industry. The course is not about git and all that it can do but rather we are using it as a tool to distribute code to you as well as how you will be submitting your project/laboratory implementations.  

Git is an extremely powerful tool and while we will only be using a very small set of its features if you would like to learn more start [here](https://www.atlassian.com/git/tutorials/what-is-git). To get started using git you will need to install in on your system. [Here](https://www.atlassian.com/git/tutorials/install-git) is a link to how to install git on a Mac, Windows or Linux system.  

__NOTE:__ There are countless git clients that provide a graphical user interface for you to use. In fact Android Studio provides a built in git client itself. We, however, will be focusing on using git from the command line.  

### Our git workflow  
You will need to clone this repository on your local development machine. In addition to that you will also need to add an additional remote repository that we will refer to as `upstream`. When cloning your repository and adding additional remotes, you must decide on whether you want to use `ssh` or `https` protocals. The primary difference is that if you use `https` you will need to enter your diretory ID and password everytime you push to your origin remote. If you have already added a ssh key to your university gitlab account you can still use it. If you do not have a ssh key and would like to add one follow the steps in this [document](https://gitlab.cs.umd.edu/help/ssh/README#generating-a-new-ssh-key-pair). The suggested type of key to generate is a [1024-bit RSA key](https://gitlab.cs.umd.edu/help/ssh/README#rsa-ssh-keys).  

The following set-up steps assume https as the chosen protocal for cloning your git repo and adding upstream.  
1. clone your repo locally wherever you plan on doing your development work for the course.  
Navigate to the directory where you want to clone your repository and run the following command: `git clone https://gitlab.cs.umd.edu/cmsc436fall2020/cmsc436-<your-directory-id>.git`. When this command is complete you will now see a new directory that will have the name `cmsc436-<your-directory-id>`.  
2.  Navigate into your new cloned repository. From the command line you would enter: `cd cmsc436-<your-directory-id>`.  
3.  Run `git remote -v`. You should see an output like the following:  
```
origin	https://gitlab.cs.umd.edu/cmsc436fall2020/cmsc436-<your-directory-id>.git (fetch)
origin	https://gitlab.cs.umd.edu/cmsc436fall2020/cmsc436-<your-directory-id>.git (push)
```  
`origin` is the label given to the url that points to your repository running on the universitry gitlab server. There is direct connection between your repository on your local machine and the repository that `origin` points to. Your repository is private and can only be viewed by yourself and members of the teaching staff. It is where you will do all of your coding and where you submit your work by comitting and pushing those changes to your `origin`. So how will you get your assignments as they are released?  
4.  From inside of your repository directory, run the following command:  
`git remote add upstream https://gitlab.cs.umd.edu/arasevic/cmsc436fall2020-student.git`. Once that is complete run `git remote -v` again. You should see the following output:  
```
origin	https://gitlab.cs.umd.edu/cmsc436fall2020/cmsc436-<your-directory-id>.git (fetch)
origin	https://gitlab.cs.umd.edu/cmsc436fall2020/cmsc436-<your-directory-id>.git (push)
upstream	https://gitlab.cs.umd.edu/arasevic/cmsc436fall2020-student.git (fetch)
upstream	https://gitlab.cs.umd.edu/arasevic/cmsc436fall2020-student.git (push)
```  
__NOTE:__ while the origin url will vary and be unique for everyone the upstream will be identical for everyone.  

`upstream` is a common repository that everyone will be able to `pull` from and add new files and changes to existing files in their own repositories. Your access to this repository is read only - in terms of git commands this is analogous to `git pull`. You can write to your origin repository and this is analagous to `git push`. The flow for the course is we will add new started code for projects/exercises to `upstream` and then to receive them you will issue the following command:  
`git pull upstream master` .  
This command will add the changes in the `upstream` repository to your local copy of your repository. At this point your local repository and your `origin` will be different. You can verify this by running `git status`. You should see something along the lines of your local repository is 1 (or more) commits ahead of origin master. To resolve this you just need to run:  
 `git push origin master`. Once that is done your local repository and your remote `origin` will be the same.  

 ### submitting your work  
 Saving your files is not enough to be able to `push` your changes to your `origin`. Git doesn't track every file save you do - it only tracks commits. In order to commit your changes you should start with the following:  
 `git status`  

 `git status` will tell you if there are any chnages to files git is tracking or if there are new files present in your repository that git is not tracking (i.e. you've added a new kotln class to comlete your laboratory assignemnt). In either scenario you need to execute the following:  
 `git add relative/path/to/file/you/want/to/commit`  
 Since you are already in the directory that contains your repository there is no need to do absolute paths. Once you have added all of your files to be committed then you simply run:  
 `git commit -m "your commit message"`  
 The text you type inside of the parentheses will be attached to that specific commit and can be viewd in your commit history by running `git log`. When you are committing the code that you want to count as your submission for an assignment you should enter something similar to "completing Activity Lab". The final step is to then run `git push origin master`. If you do not push to your origin we will not be able to look at your code.  

 __NOTE:__ When submitting assignments always log into the [university gitlab server](https://gitlab.cs.umd.edu) after pushing. This is a sanity check and will prevent you from reveiving a zero because you thought you pushed after committing but in fact did not. It is also important to realize that if you get in the habit of pushing to your origin often you will always have a back-up of your work in case your local machine's hard drive fails.


### Git commands to know
1.  `git status` ([reference](https://www.atlassian.com/git/tutorials/inspecting-a-repository)) 
2.  `git add` ([reference](https://www.atlassian.com/git/tutorials/saving-changes)) 
3.  `git commit` ([reference](https://www.atlassian.com/git/tutorials/saving-changes/git-commit)) 
4.  `git pull`  ([reference](https://www.atlassian.com/git/tutorials/syncing/git-pull))
5.  `git push` ([reference](https://www.atlassian.com/git/tutorials/syncing/git-push)) 
6.  `git remote -v`  ([reference](https://www.atlassian.com/git/tutorials/syncing))
7.  `git remote add upstream <upstream_url>` ([reference](https://docs.github.com/en/github/using-git/adding-a-remote))

