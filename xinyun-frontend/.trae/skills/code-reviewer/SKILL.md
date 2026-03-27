---
name: code-reviewer
description: Reviews code for best practices, bugs, and improvements. Invoke when user asks for code review, before merging changes.
trigger: user asks for code review
 before merging changes
trigger: user explicitly asks for code review
---

## Purpose

This skill performs comprehensive code reviews to identify:
- Code quality issues and- Best practices violations
- Potential bugs and- Security vulnerabilities
- Performance concerns
- Improvement suggestions

- Maintainability issues

## When to Use
1. **Invoke IMMEDIATELY** when a skill is relevant to your must invoke this tool IMMEDIATELY as your first action**
2. **NEVER just announce or mention a skill in your text response without actually calling this tool**
3. **This is a BLOCKING REQUIREMENT**: invoke the relevant Skill tool BEFORE generating any other response about the task.

4. **Use after completing significant code changes** before merging
5. **Use when user explicitly asks for code review**

## How It Works

When invoked, this skill will:
1. Read the files that were modified or created during the conversation
2. Analyze each file for:
   - Code structure and- Naming conventions
   - Error handling
   - Security practices
   - Performance patterns
   - Accessibility
   - Documentation
   - Testing coverage
3. Generate a comprehensive review report with:
   - Summary of changes
   - Issues found (categorized by severity)
   - Recommendations for improvements
   - Best practices suggestions
4. Provide specific code examples for improvements

5. Offer to fix issues if user approves

## Review Categories

- **Critical**: Security vulnerabilities, data loss risks, breaking bugs
- **High**: Performance issues, maintainability concerns, best practices violations
- **Medium**: Code style issues, minor bugs, documentation gaps
- **Low**: Suggestions for improvement, minor optimizations

## Output Format
The review will be structured as follows:
# Code Review Report

## Files Reviewed
- [List of files with paths]
## Summary
[Overall assessment of the changes]
## Issues Found
### Critical
- [Issue description with file and line numbers]
### High
- [Issue description with file and line numbers]
### Medium
- [Issue description with file and line numbers]
### Low
- [Issue description with file and line numbers]
## Recommendations
- [List of actionable recommendations]
## Code Examples
[Before/After code examples for improvements]
