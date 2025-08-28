async function loadResume() {
    try {
        const response = await fetch('/data/data.json', { cache: 'no-store' });
        const data = await response.json();

        // Name and Role
        document.getElementById('name').textContent = data.name || '';
        document.getElementById('role').textContent = data.role || '';

        // Contact
        const contactList = document.getElementById('contact-list');
        contactList.innerHTML = '';
        const contacts = [
            { icon: '📧', text: data.contact?.email },
            { icon: '📱', text: data.contact?.phone },
            { icon: '📍', text: data.contact?.location },
            { icon: '🔗', text: data.contact?.linkedin }
        ];
        contacts.forEach(item => {
            if (!item.text) return;
            const row = document.createElement('div');
            row.className = 'contact-item';
            const icon = document.createElement('span');
            icon.className = 'icon';
            icon.textContent = item.icon;
            const span = document.createElement('span');
            span.textContent = item.text;
            row.appendChild(icon);
            row.appendChild(span);
            contactList.appendChild(row);
        });

        // Education
        const educationList = document.getElementById('education-list');
        educationList.innerHTML = '';
        (data.education || []).forEach(ed => {
            const wrap = document.createElement('div');
            wrap.className = 'education-item';
            const h4 = document.createElement('h4');
            h4.textContent = ed.degree || '';
            const pSchool = document.createElement('p');
            pSchool.textContent = ed.school || '';
            const pYear = document.createElement('p');
            pYear.className = 'year';
            pYear.textContent = ed.year || '';
            wrap.appendChild(h4);
            wrap.appendChild(pSchool);
            wrap.appendChild(pYear);
            educationList.appendChild(wrap);
        });

        // Certifications
        const certsList = document.getElementById('certs-list');
        certsList.innerHTML = '';
        (data.certifications || []).forEach(cert => {
            const wrap = document.createElement('div');
            wrap.className = 'cert-item';
            const h4 = document.createElement('h4');
            h4.textContent = cert.name || '';
            const pYear = document.createElement('p');
            pYear.className = 'year';
            pYear.textContent = cert.year || '';
            wrap.appendChild(h4);
            wrap.appendChild(pYear);
            certsList.appendChild(wrap);
        });

        // Skills
        const skillsList = document.getElementById('skills-list');
        skillsList.innerHTML = '';
        (data.skills || []).forEach(group => {
            const wrap = document.createElement('div');
            wrap.className = 'skill-category';
            const h4 = document.createElement('h4');
            h4.textContent = group.category || '';
            const tags = document.createElement('div');
            tags.className = 'skill-tags';
            (group.items || []).forEach(skill => {
                const tag = document.createElement('span');
                tag.className = 'skill-tag';
                tag.textContent = skill;
                tags.appendChild(tag);
            });
            wrap.appendChild(h4);
            wrap.appendChild(tags);
            skillsList.appendChild(wrap);
        });

        // Profile
        const profileText = document.getElementById('profile-text');
        if (profileText) profileText.textContent = data.profile || '';

        // Experience
        const expList = document.getElementById('experience-list');
        expList.innerHTML = '';
        (data.experience || []).forEach(job => {
            const item = document.createElement('div');
            item.className = 'experience-item';

            const header = document.createElement('div');
            header.className = 'experience-header';

            const title = document.createElement('h4');
            title.textContent = job.title || '';
            const company = document.createElement('span');
            company.className = 'company';
            company.textContent = job.company || '';
            const duration = document.createElement('span');
            duration.className = 'duration';
            duration.textContent = job.duration || '';

            header.appendChild(title);
            header.appendChild(company);
            header.appendChild(duration);

            const list = document.createElement('ul');
            (job.bullets || []).forEach(b => {
                const li = document.createElement('li');
                li.textContent = b;
                list.appendChild(li);
            });

            item.appendChild(header);
            item.appendChild(list);
            expList.appendChild(item);
        });

        // Projects
        const projectsList = document.getElementById('projects-list');
        projectsList.innerHTML = '';
        (data.projects || []).forEach(proj => {
            const item = document.createElement('div');
            item.className = 'project-item';

            const header = document.createElement('div');
            header.className = 'project-header';

            const title = document.createElement('h4');
            title.textContent = proj.title || '';
            const stack = document.createElement('span');
            stack.className = 'tech-stack';
            stack.textContent = (proj.tech_stack || []).join(', ');

            header.appendChild(title);
            header.appendChild(stack);

            const desc = document.createElement('p');
            desc.textContent = proj.description || '';

            item.appendChild(header);
            item.appendChild(desc);
            projectsList.appendChild(item);
        });

    } catch (err) {
        console.error('Failed to load resume data:', err);
    }
}

window.addEventListener('DOMContentLoaded', loadResume); 