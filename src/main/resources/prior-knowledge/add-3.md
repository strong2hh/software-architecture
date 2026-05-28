# Attribute-Driven Design (ADD) Method

## Step 1 Review Inputs
The first step of the ADD method involves reviewing the inputs and identifying which requirements will be considered as architectural drivers.

## Step 2 Establish the Iteration Goal by Selecting Drivers
A design round generally takes the form of a series of design iterations, where each iteration focuses on achieving a particular goal. Such a goal typically involves designing to satisfy a subset of the drivers.

## Step 3 Choose One or More Elements of the System to Refine
This step is where the core design activities start. The elements that you will select are the ones that are involved in the satisfaction of specific drivers. For greenfield development, you can start by establishing the system context and then selecting the only available element, that is, the system itself, for refinement by decomposition. For existing systems or for later design iterations in greenfield systems, you would normally choose to refine elements that were identified in prior iterations.

## Step 4 Choose One or More Design Concepts That Satisfy the Selected Drivers
This step requires you to identify alternatives among design concepts that can be used to achieve your iteration goal, and to select one of these alternatives.

## Step 5 Instantiate Architectural Elements, Allocate Responsibilities, and Define Interfaces
This step requires instantiating architectural elements based on the selected design concepts and assigning responsibilities to them. In addition, relationships and interfaces between elements need to be established so that they can collaborate and exchange information effectively.

## Step 6 Sketch Views and Record Design Decisions
At this point, you have completed the design activities for the iteration. In addition to preserving the views, the representations of the structures you created, you should also record the significant decisions made during the design iteration, as well as the reasons behind those decisions, to facilitate later analysis and understanding.

## Step 7 Perform Analysis of Current Design and Review Iteration Goal and Achievement of Design Purpose
This step checks whether a partial design that satisfies the goals of the current iteration has been created, and considers whether additional design iterations are needed.
